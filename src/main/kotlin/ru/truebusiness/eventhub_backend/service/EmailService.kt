package ru.truebusiness.eventhub_backend.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.logger

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    private val log by logger()

    fun sendConfirmationCode(code: String, to: String?) {
        log.info("Sending confirmation code: {} on email: {}", code, to)
        to?.let { email ->
            val message = SimpleMailMessage()
            message.setTo(email)
            message.subject = "Confirm your registration to EventHub"
            message.text = """
                Hello! 
                
                Please confirm your registration by clicking the link below:
                http://localhost:8080/auth/check-code/$code
                """.trimIndent()
            mailSender.send(message)
            log.info("Successfully sent to email $email")
        } ?: log.error("Failed to send confirmation code!")
    }
}