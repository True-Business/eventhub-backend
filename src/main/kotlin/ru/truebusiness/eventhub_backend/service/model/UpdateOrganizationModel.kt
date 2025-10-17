package ru.truebusiness.eventhub_backend.service.model

import java.util.UUID

class UpdateOrganizationModel (
    var id: UUID,
    var name: String?,
    var description: String?,
    var address: String?,
    var pictureUrl: String?
)