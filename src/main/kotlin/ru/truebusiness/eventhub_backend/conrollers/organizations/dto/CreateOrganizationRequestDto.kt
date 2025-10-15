package ru.truebusiness.eventhub_backend.conrollers.organizations.dto

import java.util.UUID

data class CreateOrganizationRequestDto(
    val name: String,
    val description: String,
    val address: String?,
    val pictureUrl: String?,
    //todo: убрать creatorId, когда пробросим хедеры
    val creatorId: UUID
)