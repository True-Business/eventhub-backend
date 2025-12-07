package ru.truebusiness.eventhub_backend.service.model

import java.time.Instant
import java.util.UUID

class PostModel(
    var id: UUID,
    var eventId: UUID,
    var createdAt: Instant,
    var modifiedAt: Instant?,
    var text: String
)