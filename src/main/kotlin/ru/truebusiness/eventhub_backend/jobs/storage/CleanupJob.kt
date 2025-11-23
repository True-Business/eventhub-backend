package ru.truebusiness.eventhub_backend.jobs.storage

import java.util.concurrent.TimeUnit
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.truebusiness.eventhub_backend.service.storage.MinioStorageService

@Component
class CleanupJob(
    private val minioStorageService: MinioStorageService
) {
    @Scheduled(
        fixedRateString = "\${app.storage.cleanupJob.delayMinutes}",
        timeUnit = TimeUnit.MINUTES
    )
    fun cleanupExpiredMetadata() {
        minioStorageService.deleteExpiredMetadata()
        minioStorageService.cleanupDeletedObjects()
    }
}