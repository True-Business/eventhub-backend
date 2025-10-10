package ru.truebusiness.eventhub_backend.conrollers.dto

data class UpdateUserRequestDto(
    var username: String,
    var shortId: String,
    var bio: String
)