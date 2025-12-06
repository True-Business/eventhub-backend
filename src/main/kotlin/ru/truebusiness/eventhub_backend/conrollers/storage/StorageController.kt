package ru.truebusiness.eventhub_backend.conrollers.storage

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectConfirm
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectDownload
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectUpload
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectsList
import ru.truebusiness.eventhub_backend.service.storage.MinioStorageService

@RestController
@RequestMapping("/api/v1/storage")
class StorageController(
    private val minioStorageService: MinioStorageService
) {
    @PostMapping("/presigned-urls")
    fun genUploadUrls(
        @RequestBody
        request: ObjectUpload.Request
    ): ObjectUpload.Response {
        val urls = minioStorageService.genUploadUrls(request)
        return ObjectUpload.Response(urls)
    }

    @PostMapping("/confirmed")
    fun confirmUpload(
        @RequestBody
        request: ObjectConfirm.Request
    ): ObjectConfirm.Response {
        return minioStorageService.confirmUpload(request)
    }

    @PostMapping("/confirmed/list")
    fun listConfirmed(
        @RequestBody
        request: ObjectsList.Request
    ): ObjectsList.Response {
        return minioStorageService.listObjects(request);
    }

    @GetMapping
    fun getDownloadUrls(
        @RequestBody
        request: ObjectDownload.Request
    ): ObjectDownload.Response {
        return minioStorageService.genDownloadUrls(request)
    }

}