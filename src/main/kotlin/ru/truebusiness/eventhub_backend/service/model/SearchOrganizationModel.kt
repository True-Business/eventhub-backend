package ru.truebusiness.eventhub_backend.service.model

data class SearchOrganizationModel(
    val search: String,
    val creatorShortId: String?,
    val address: String?,
    val onlyVerified: Boolean,
    val onlySubscribed: Boolean,
    val onlyAdministrated: Boolean
)
