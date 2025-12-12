package ru.truebusiness.eventhub_backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.service.model.EventModel

@Service
class EmailService(
    @param:Value("\${spring.mail.username}")
    private val emailLogin: String,
    private val mailSender: JavaMailSender,
) {
    private val log by logger()

    fun sendConfirmationCode(code: String, to: String) {
        log.info("Sending confirmation code: {} on email: {}", code, to)

        val subject = "Confirm your registration to EventHub"
        val text = """
                Hello! 
                
                Your confirmation code is: $code
                """.trimIndent()

        sendMessage(to, subject, text)
    }

    fun sendEventCancellation(toEmail: String, event: EventModel) {
        val subject = "Event cancellation"
        val text = """
                Sorry, the event "${event.name}" has been cancelled.
                """.trimIndent()

        sendMessage(toEmail, subject, text)
    }

    private fun sendMessage(toEmail: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setTo(toEmail)
        message.from = emailLogin
        message.subject = subject
        message.text = text

        mailSender.send(message)
    }
}