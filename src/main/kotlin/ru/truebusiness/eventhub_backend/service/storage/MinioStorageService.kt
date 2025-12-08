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
import kotlin.getValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.Page
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectConfirm
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectDownload
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectUpload
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectsList
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.ObjectMetadataMapper
import ru.truebusiness.eventhub_backend.repository.storage.FileStatus
import ru.truebusiness.eventhub_backend.repository.storage.S3ObjectMetadata
import ru.truebusiness.eventhub_backend.repository.storage.S3objectMetadataRepository

private val initialFileStatus = FileStatus.PENDING
private val sort = Sort.by("confirmedAt").descending()
private val defaultPage = PageRequest.of(0, 20, sort)
@Service
class MinioStorageService(
    private val minioClient: MinioClient,
    private val s3ObjectMetadataRepository: S3objectMetadataRepository,
    private val objectMetadataMapper: ObjectMetadataMapper,
    @param:Value("\${app.storage.bucket.name}")
    private val bucket: String,
    @param:Value("\${app.storage.bucket.nonConfirmedExpiry}")
    private val expiry: Duration,
) {
    private val log by logger()

    /**
     * Первый этап загрузки файлов. клиент шлет названия файлов(origin) и
     * получает в соответствие к каждому файлу ссылку на его загружку в s3. К
     * каждому origin - id метадаты и ссылку на загрузку в s3.
     *
     * Метадата если не будет подтверждена, сотрется gc.
     *
     * Неподтвержденные файлы сохраняются с префиксом <code>FileStatus
     * .PENDING</code>, должны быть стерты политикой жизненного цикла бакета
     * по истечении определенного времени.
     */
    @Transactional
    fun genUploadUrls(request: ObjectUpload.Request): ObjectUpload.Response {
        StorageUtils.validateOrigin(request.originNames)

        val (ownerId, ownerType, fileNames) = request
        val expiry = Instant.now().plus(expiry)
        return fileNames.map {
            val id = UUID.randomUUID();
            val objectPath = StorageUtils.getFilePath(
                initialFileStatus, getObjectName(it, id)
            )
            PresignedUrlInfo(
                id,
                it,
                objectPath,
                bucket,
                getUploadUrl(objectPath),
            )
        }.map { urlInfo ->
            S3ObjectMetadata(
                id = urlInfo.id,
                origin = urlInfo.origin,
                bucket = urlInfo.bucket,
                ownerId = ownerId,
                ownerType = ownerType,
                status = initialFileStatus,
                expiry = expiry,
                url = urlInfo.url
            )
        }.also(
            s3ObjectMetadataRepository::saveAll
        ).map(
            objectMetadataMapper::s3ObjectMetadata2UrlInfo
        ).let{
            ObjectUpload.Response(it)
        }
    }

    /**
     * Второй этпа загрузки объектов в s3 шлем подтверждение. Смотрим на
     * статус метадаты
     *
     * <code>FileStatus.PENDING</code> - пробуем поменять префикс на
     * <code>FileStatus.CONFIRMED</code>, убираем срок истечения и ставим
     * дату подтверждения.
     *
     * Если не получилось поменять префикс, файл считается не подтвержденным,
     * ждем пока метадату удалит gc.
     *
     * <code>FileStatus.CONFIRMED</code> - просто сообщаем что загрузка
     * подтверждена
     *
     * <code>FileStatus.DELETED</code> - сообщаем что файл не найдет,
     * метадату начал удалять gc
     */
    @Transactional
    fun confirmUpload(request: ObjectConfirm.Request): ObjectConfirm.Response {
        val (ownerId, ownerType, ids) = request
        val metas = s3ObjectMetadataRepository.findAllById(ids).also {
            StorageUtils.validateMetasOwner(it, ownerId, ownerType)
        }

        val metasById = metas.associateBy { it.id }
        val confirmedAt = Instant.now();
        val confInfo = ids.map {
            metasById[it]?.let { meta ->
                when (meta.status) {
                    FileStatus.PENDING   -> {
                        moveFileToPermanent(meta).also { moveInfo ->
                            if (moveInfo.status == ObjectConfirm.FileConfirm.UPLOADED) {
                                meta.status = FileStatus.CONFIRMED
                                meta.expiry = null
                                meta.confirmedAt = confirmedAt
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

    /**
     * Находим метадату файлов, если статус
     * <code>FileStatus.CONFIRMED</code>, генерируем ссылку для загрузки, иначе null
     */
    @Transactional
    fun genDownloadUrls(req: ObjectDownload.Request): ObjectDownload.Response {
        val metas = s3ObjectMetadataRepository.findAllById(req.ids)
        val metasById = metas.associateBy { it.id }
        val urls = metas.map {
            metasById[it.id]?.let { meta ->
                when (meta.status) {
                    FileStatus.CONFIRMED -> {
                        val objPath = StorageUtils.getFilePath(
                            it.status, getObjectName(it.origin, it.id)
                        )
                        ObjectDownload.UrlInfo(it.id, getDownloadUrl(objPath))
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

    /**
     * Получение всех подтржденных файлов для владельца заданного типа
     */
    fun listObjects(request: ObjectsList.Request): ObjectsList.Response {
        val page = getObjectsListPage(request.page)
        val objects = s3ObjectMetadataRepository.findAllByOwnerIdAndOwnerType(
            ownerId = request.ownerId,
            ownerType = request.ownerType,
            pageable = page,
        ).let(objectMetadataMapper::s3ObjectMetadata2ObjectListObject)
        return ObjectsList.Response(objects)
    }

    @Transactional
    fun deleteMetadata(idx: List<UUID>) {
        val metasById = s3ObjectMetadataRepository.findAllById(idx)
        metasById.forEach { it.status = FileStatus.DELETED }
        s3ObjectMetadataRepository.saveAll(metasById)
    }

    /**
     * Маркируем метадату как <code>FileStatus.DELETED</code>, удаление
     * произойдет в отдельном методе
     * */
    @Transactional
    fun deleteExpiredMetadata() {
        val metasById =
            s3ObjectMetadataRepository.findAllByExpiryBeforeAndStatus(
                Instant.now(), FileStatus.PENDING
            )
        metasById.forEach {
            it.status = FileStatus.DELETED
        }
        s3ObjectMetadataRepository.saveAll(metasById)
    }

    /**
     * Находим все файлы с статусом <code>FileStatus.DELETED</code>? удаляем
     * объект с постоянного хранения в s3, удаляем метадату.
     */
    @Transactional
    fun cleanupDeletedObjects() {
        val metas = s3ObjectMetadataRepository.findAllByStatusIs(
            FileStatus.DELETED
        )
        metas.forEach {
            val objPath = StorageUtils.getFilePath(
                FileStatus.CONFIRMED, getObjectName(it.origin, it.id)
            )

            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(objPath)
                    .build()
            )
        }

        s3ObjectMetadataRepository.deleteAllById(metas.map { it.id })
        log.info("cleanup deleted objects ${metas.size}")
    }

    private fun moveFileToPermanent(
        meta: S3ObjectMetadata
    ): ObjectConfirm.ConfirmInfo {
        val tmpObject = meta.let {
            StorageUtils.getFilePath(
                it.status, getObjectName(it.origin, it.id)
            )
        }
        val permObject = StorageUtils.getFilePath(
            FileStatus.CONFIRMED, getObjectName(meta.origin, meta.id),
        )
        return try {
            val source =
                CopySource.builder().bucket(bucket).`object`(tmpObject).build()

            val args =
                CopyObjectArgs.builder().source(source).bucket(bucket).`object`(
                    permObject
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

    private fun getObjectName(objectName: String, uuid: UUID): String {
        return "${uuid}_${objectName}"
    }
    private fun getObjectName(objectName: String): String {
        return getObjectName(objectName, UUID.randomUUID())
    }

    private fun getDownloadUrl(`object`: String): String {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .`object`(`object`)
                .expiry(expiry.toSeconds().toInt())
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
                .expiry(expiry.toSeconds().toInt())
                .build()
        )
    }

    private fun getObjectsListPage(page: Page?): Pageable {
        if (page == null) {
            return defaultPage
        }
        return PageRequest.of(page.current, page.size, sort)
    }

    private data class PresignedUrlInfo(
        val id: UUID,
        val origin: String,
        val `object`: String,
        val bucket: String,
        var url: String,
    )
}