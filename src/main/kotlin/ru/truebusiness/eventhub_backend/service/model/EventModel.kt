package ru.truebusiness.eventhub_backend.service.model

import java.time.Instant
import java.util.UUID

class EventModel(
    var name: String,
    var startDateTime: Instant,
    var endDateTime: Instant?,
    var updatedAt: Instant = Instant.now(),
    var organizerId: UUID,
    var organizationId: UUID?,
    var category: EventCategoryModel,
    var address: String,
    var route: String,
    var description: String,
    var price: Double,
    var isOpen: Boolean,
    var status: EventStatusModel,
    var city: String,
    var isWithRegister: Boolean,
    var peopleLimit: Int?,
    var registerEndDateTime: Instant?
)