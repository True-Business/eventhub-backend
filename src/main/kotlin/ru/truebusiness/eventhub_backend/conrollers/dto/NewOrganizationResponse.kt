package ru.truebusiness.eventhub_backend.conrollers.dto

import java.util.*

data class NewOrganizationResponse(
    val organizationId: UUID?,
    val organizationName: String?,
    val organizationDescription: String?,
    val organizationAddress: String?,
    val organizationPicture: String?,
    val organizationCreatorId: UUID?
)