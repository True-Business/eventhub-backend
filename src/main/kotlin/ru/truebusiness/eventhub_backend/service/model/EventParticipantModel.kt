package ru.truebusiness.eventhub_backend.service.model

import java.time.Instant
import java.util.UUID

class EventParticipantModel(
    var id: UUID,
    var userId: UUID,
    var eventId: UUID,
    var registeredAt: Instant
)