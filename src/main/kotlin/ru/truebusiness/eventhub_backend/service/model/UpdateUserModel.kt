package ru.truebusiness.eventhub_backend.service.model

import java.util.UUID

class UpdateUserModel(
    var id: UUID,
    var username: String,
    var shortId: String,
    var bio: String
)