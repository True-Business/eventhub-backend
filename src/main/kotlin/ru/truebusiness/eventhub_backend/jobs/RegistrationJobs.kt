package ru.truebusiness.eventhub_backend.jobs

import org.springframework.scheduling.annotation.Scheduled
import ru.truebusiness.eventhub_backend.repository.ConfirmationCodeRepository
import java.time.Instant
import java.util.concurrent.TimeUnit

class RegistrationJobs(
    private val confirmationCodeRepository: ConfirmationCodeRepository,
) {
    @Scheduled(
        fixedDelayString = "\${app.registration.cleanupIntervalMinutes}",
        timeUnit = TimeUnit.MINUTES
    )
    fun cleanupConfirmationCodes() {
        confirmationCodeRepository.deleteByExpiresAtBefore(Instant.now())
    }
}