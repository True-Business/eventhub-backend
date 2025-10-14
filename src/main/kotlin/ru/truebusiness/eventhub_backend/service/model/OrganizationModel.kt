package ru.truebusiness.eventhub_backend.service.model

import java.util.UUID

class OrganizationModel (
    var name: String = "",
    var description: String = "",

    var address: String?,
    var pictureUrl: String?,

    var isVerified: Boolean = false,
    var creatorId: UUID
)