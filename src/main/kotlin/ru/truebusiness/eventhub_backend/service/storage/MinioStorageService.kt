package ru.truebusiness.eventhub_backend.service.storage

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import jakarta.transaction.Transactional
import java.util.UUID
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectUpload
import ru.truebusiness.eventhub_backend.mapper.ObjectMetadataMapper
import ru.truebusiness.eventhub_backend.repository.storage.S3ObjectMetadata
import ru.truebusiness.eventhub_backend.repository.storage.S3objectMetadataRepository

@Service
class MinioStorageService(
    val minioClient: MinioClient,
    val s3ObjectMetadataRepository: S3objectMetadataRepository,
    val objectMetadataMapper: ObjectMetadataMapper,
    val bucket: String,
    val expiry: Int,
) {
    @Transactional
    fun genPresignedUrls(request: ObjectUpload.Request): List<ObjectUpload.Response> {
        val (ownerId, ownerType, fileNames) = request
        return fileNames.map {
            PresignedUrlInfo(
                it, getObjectName(it), bucket, getPresignedUrl(it)
            )
        }.map { (origin, `object`, bucket) ->
            S3ObjectMetadata(
                origin = origin,
                `object` = `object`,
                bucket = bucket,
                ownerId = ownerId,
                ownerType = ownerType,
            )
        }.let(
            s3ObjectMetadataRepository::saveAll
        ).map(
            objectMetadataMapper::s3ObjectMetadata2ObjectUploadResponse
        )
    }

    @Transactional
    fun confirmUpload(request: ObjectUpload.Request): ObjectUpload.Response {

    }

    private fun getObjectName(objectName: String): String {
        return "${objectName}_${UUID.randomUUID()}"
    }

    private fun getPresignedUrl(
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