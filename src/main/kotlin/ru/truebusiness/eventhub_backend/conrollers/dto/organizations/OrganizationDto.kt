package ru.truebusiness.eventhub_backend.conrollers.dto.organizations

import java.util.UUID

data class OrganizationDto(
    val id: UUID,
    val name: String,
    val description: String,
    val address: String?,
    val pictureUrl: String?,
    val creatorId: UUID
)