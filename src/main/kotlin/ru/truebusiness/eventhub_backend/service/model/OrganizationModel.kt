package ru.truebusiness.eventhub_backend.service.model

import java.util.UUID

class OrganizationModel (
    val name: String = "",
    val description: String = "",

    val address: String?,
    val pictureUrl: String?,

    val isVerified: Boolean = false,
    var creatorId: UUID?
)