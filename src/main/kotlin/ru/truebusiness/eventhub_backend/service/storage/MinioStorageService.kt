package ru.truebusiness.eventhub_backend.service.storage

import io.minio.CopyObjectArgs
import io.minio.CopySource
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.RemoveObjectArgs
import io.minio.errors.ErrorResponseException
import io.minio.http.Method
import jakarta.transaction.Transactional
import java.time.Duration
import java.time.Instant
import java.util.UUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectConfirm
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectDownload
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectUpload
import ru.truebusiness.eventhub_backend.mapper.ObjectMetadataMapper
import ru.truebusiness.eventhub_backend.repository.storage.FileStatus
import ru.truebusiness.eventhub_backend.repository.storage.S3ObjectMetadata
import ru.truebusiness.eventhub_backend.repository.storage.S3objectMetadataRepository

private val initialFileStatus = FileStatus.PENDING

@Service
class MinioStorageService(
    private val minioClient: MinioClient,
    private val s3ObjectMetadataRepository: S3objectMetadataRepository,
    private val objectMetadataMapper: ObjectMetadataMapper,
    @param:Value("\${app.storage.bucket.name}")
    private val bucket: String,
    @param:Value("\${app.storage.bucket.nonConfirmedExpiryMinutes}")
    private val expiry: Int,
) {
    private val ttl = Duration.ofMinutes(expiry.toLong())

    @Transactional
    fun genUploadUrls(request: ObjectUpload.Request): List<ObjectUpload.UrlInfo> {
        StorageUtils.validateOrigin(request.originNames)

        val (ownerId, ownerType, fileNames) = request
        val expiry = Instant.now().plus(ttl)
        return fileNames.map {
            val objectPath = StorageUtils.getFilePath(
                initialFileStatus, getObjectName(it)
            )
            PresignedUrlInfo(
                it,
                objectPath,
                bucket,
                getUploadUrl(it),
            )
        }.map { (origin, `object`, bucket) ->
            S3ObjectMetadata(
                origin = origin,
                `object` = `object`,
                bucket = bucket,
                ownerId = ownerId,
                ownerType = ownerType,
                status = initialFileStatus,
                expiry = expiry,
            )
        }.let(
            s3ObjectMetadataRepository::saveAll
        ).map(
            objectMetadataMapper::s3ObjectMetadata2UrlInfo
        )
    }

    @Transactional
    fun confirmUpload(request: ObjectConfirm.Request): ObjectConfirm.Response {
        val (ownerId, ownerType, ids) = request
        val metas = s3ObjectMetadataRepository.findAllById(ids).also {
            StorageUtils.validateMetasOwner(it, ownerId, ownerType)
        }

        val metasById = metas.associateBy { it.id }
        val confInfo = ids.map {
            metasById[it]?.let { meta ->
                when (meta.status) {
                    FileStatus.PENDING   -> {
                        moveFileToPermanent(meta).also { moveInfo ->
                            if (moveInfo.status == ObjectConfirm.FileConfirm.UPLOADED) {
                                meta.status = FileStatus.CONFIRMED
                            }
                        }
                    }

                    FileStatus.CONFIRMED -> {
                        ObjectConfirm.ConfirmInfo(
                            meta.id, ObjectConfirm.FileConfirm.UPLOADED
                        )
                    }

                    FileStatus.DELETED   -> {
                        ObjectConfirm.ConfirmInfo(
                            meta.id, ObjectConfirm.FileConfirm.NOT_FOUND
                        )
                    }
                }
            } ?: ObjectConfirm.ConfirmInfo(
                it, ObjectConfirm.FileConfirm.NOT_FOUND
            )
        }
        s3ObjectMetadataRepository.saveAll(metas)

        return ObjectConfirm.Response(confInfo)
    }

    @Transactional
    fun genDownloadUrls(req: ObjectDownload.Request): ObjectDownload.Response {
        val metas = s3ObjectMetadataRepository.findAllById(req.ids)
        val metasById = metas.associateBy { it.id }
        val urls = metas.map {
            metasById[it.id]?.let { meta ->
                when (meta.status) {
                    FileStatus.CONFIRMED -> {
                        val objPath = StorageUtils.getFilePath(
                            it.status, it.`object`
                        )
                        ObjectDownload.UrlInfo(it.id, objPath)
                    }

                    else                 -> ObjectDownload.UrlInfo(
                        it.id, null
                    )
                }
            } ?: ObjectDownload.UrlInfo(
                it.id, null
            )
        }
        return ObjectDownload.Response(urls)
    }

    @Transactional
    fun deleteMetadata(idx: List<UUID>) {
        val metasById = s3ObjectMetadataRepository.findAllById(idx)
        metasById.forEach { it.status = FileStatus.DELETED }
        s3ObjectMetadataRepository.saveAll(metasById)
    }

    @Transactional
    fun deleteExpiredMetadata() {
        val metasById = s3ObjectMetadataRepository.findAllByExpiryBefore(
            Instant.now()
        )
        metasById.forEach { it.status = FileStatus.DELETED }
        s3ObjectMetadataRepository.saveAll(metasById)
    }

    @Transactional
    fun cleanupDeletedObjects() {
        val metas = s3ObjectMetadataRepository.findAllByStatusIs(
            FileStatus.DELETED
        )
        metas.forEach {
            val objPath = StorageUtils.getFilePath(
                FileStatus.CONFIRMED, it.`object`
            )

            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(objPath)
                    .build()
            )
        }

        s3ObjectMetadataRepository.deleteAllById(metas.map { it.id })
    }

    private fun moveFileToPermanent(
        meta: S3ObjectMetadata
    ): ObjectConfirm.ConfirmInfo {
        val tmpObject = StorageUtils.getFilePath(
            meta.status, meta.`object`,
        )
        val permObject = StorageUtils.getFilePath(
            FileStatus.CONFIRMED, meta.`object`,
        )
        return try {
            val source =
                CopySource.builder().bucket(bucket).`object`(tmpObject).build()

            val args =
                CopyObjectArgs.builder().source(source).bucket(bucket).`object`(
                    StorageUtils.getFilePath(
                        FileStatus.CONFIRMED, permObject,
                    )
                ).build()

            minioClient.copyObject(args)
            ObjectConfirm.ConfirmInfo(
                meta.id, ObjectConfirm.FileConfirm.UPLOADED
            )
        } catch (_: ErrorResponseException) {
            ObjectConfirm.ConfirmInfo(
                meta.id, ObjectConfirm.FileConfirm.NOT_FOUND
            )
        } finally {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(tmpObject)
                    .build()
            )
        }
    }

    private fun getObjectName(objectName: String): String {
        return "${objectName}_${UUID.randomUUID()}"
    }

    private fun getDownloadUrl(`object`: String): String {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .`object`(`object`)
                .expiry(expiry)
                .build()
        )
    }

    private fun getUploadUrl(
        `object`: String
    ): String {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(bucket)
                .`object`(`object`)
                .expiry(expiry)
                .build()
        )
    }

    private data class PresignedUrlInfo(
        val origin: String,
        val `object`: String,
        val bucket: String,
        var url: String,
    )
}