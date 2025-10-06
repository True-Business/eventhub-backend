package ru.truebusiness.eventhub_backend.conrollers.dto

data class ErrorResponseDto(
    val code: Int,
    val message: String
)