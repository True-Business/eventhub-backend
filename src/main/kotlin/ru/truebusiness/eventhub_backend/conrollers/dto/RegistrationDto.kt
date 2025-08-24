package ru.truebusiness.eventhub_backend.conrollers.dto

data class RegistrationDto(
    val shortId: String?,
    val username: String?,
    val password: String?,
    val email: String?,
)
