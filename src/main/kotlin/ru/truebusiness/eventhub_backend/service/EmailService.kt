package ru.truebusiness.eventhub_backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.logger

@Service
class EmailService(
    @param:Value("\${spring.mail.username}")
    private val emailLogin: String,
    private val mailSender: JavaMailSender,
) {
    private val log by logger()

    fun sendConfirmationCode(code: String, to: String) {
        log.info("Sending confirmation code: {} on email: {}", code, to)
        val message = SimpleMailMessage()
        message.setTo(to)
        message.from = emailLogin
        message.subject = "Confirm your registration to EventHub"
        message.text = """
                Hello! 
                
                Your confirmation code is: $code
                """.trimIndent()
        mailSender.send(message)
    }
}