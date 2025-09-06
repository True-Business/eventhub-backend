package ru.truebusiness.eventhub_backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.logger

@Service
class EmailService(
    private val mailSender: JavaMailSender,
) {
    private val log by logger()

    @Value("\${spring.mail.username}")
    private lateinit var emailLogin: String

    fun sendConfirmationCode(code: String, to: String?) {
        log.info("Sending confirmation code: {} on email: {}", code, to)
        to?.let { email ->
            val message = SimpleMailMessage()
            message.setTo(email)
            message.from = emailLogin
            message.subject = "Confirm your registration to EventHub"
            message.text = """
                Hello! 
                
                Your confirmation code is: $code
                """.trimIndent()
            mailSender.send(message)
            log.info("Successfully sent to email $email")
        } ?: log.error("Failed to send confirmation code!")
    }
}