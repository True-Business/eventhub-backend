package ru.truebusiness.eventhub_backend.jobs

import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.repository.ConfirmationCodeRepository
import java.time.Instant

@Component
class RegistrationJobs(
    private val confirmationCodeRepository: ConfirmationCodeRepository,
) {
    private val log by logger()

    @Scheduled(
        cron = "\${app.registration.cleanupJob.cron}",
        zone = "\${app.registration.cleanupJob.zone}",
    )
    @Transactional
    fun cleanupConfirmationCodes() {
        val deleted = confirmationCodeRepository.deleteByExpiresAtBefore(
            Instant.now()
        )
        log.info("Delete expired confirmation codes: {}", deleted)
    }
}