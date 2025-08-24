package ru.truebusiness.eventhub_backend.conrollers.dto

import java.time.LocalDateTime
import java.util.UUID

data class RegistrationResponse(
    val id: UUID?,
    val registrationDate: LocalDateTime?,
)
