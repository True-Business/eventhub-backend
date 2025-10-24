package ru.truebusiness.eventhub_backend.conrollers.dto

data class SearchOrganizationRequestDto(
    val search: String,
    val creatorShortId: String?,
    val address: String?,
    val onlyVerified: Boolean,
    val onlySubscribed: Boolean,
    val onlyAdministrated: Boolean
)
