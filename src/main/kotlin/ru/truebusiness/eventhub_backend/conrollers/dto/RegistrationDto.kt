package ru.truebusiness.eventhub_backend.conrollers.dto

import java.time.LocalDateTime

data class RegistrationDto(
    val shortId: String?,
    val username: String?,
    val password: String?,
    val email: String?,
    val registrationDate: LocalDateTime?,
)
