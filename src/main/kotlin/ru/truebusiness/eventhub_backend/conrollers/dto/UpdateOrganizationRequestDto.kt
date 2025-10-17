package ru.truebusiness.eventhub_backend.conrollers.dto

data class UpdateOrganizationRequestDto(
    val name: String?,
    val description: String?,
    val address: String?,
    val pictureUrl: String?
)