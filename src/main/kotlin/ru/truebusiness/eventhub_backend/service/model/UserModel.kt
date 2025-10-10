package ru.truebusiness.eventhub_backend.service.model

import java.time.Instant
import java.util.UUID

class UserModel(
    var id: UUID,
    var username: String,
    var shortId: String,
    var bio: String,
    var registrationDate: Instant,
    var isConfirmed: Boolean,
)