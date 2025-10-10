package ru.truebusiness.eventhub_backend.conrollers.dto

import java.time.Instant
import java.util.UUID

data class UserDto(
    val id: UUID,
    val username: String,
    val shortId: String,
    val bio: String,
    val registrationDate: Instant,
    val isConfirmed: Boolean,
)