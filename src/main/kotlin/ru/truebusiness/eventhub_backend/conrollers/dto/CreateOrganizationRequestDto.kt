package ru.truebusiness.eventhub_backend.conrollers.dto

import java.util.*

data class CreateOrganizationRequestDto(
    val name: String,
    val description: String,
    val address: String?,
    val picture: String?,
    //todo: убрать creatorId, когда пробросим хедеры
    val creatorId: UUID
)