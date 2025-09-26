package ru.truebusiness.eventhub_backend.conrollers.dto

import ru.truebusiness.eventhub_backend.service.model.EventCategoryModel
import ru.truebusiness.eventhub_backend.service.model.EventStatusModel
import java.time.Instant
import java.util.UUID

data class EventDto (
    var id: UUID,
    var name: String,
    var startDateTime: Instant,
    var endDateTime: Instant?,
    var updatedAt: Instant,
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
    var registerEndDateTime: Instant?,
)