package ru.truebusiness.eventhub_backend.conrollers.dto

import ru.truebusiness.eventhub_backend.service.model.EventCategoryModel
import ru.truebusiness.eventhub_backend.service.model.EventStatusModel
import java.time.Instant
import java.util.UUID

data class EventDto (
    val id: UUID,
    val name: String,
    val startDateTime: Instant,
    val endDateTime: Instant?,
    val updatedAt: Instant,
    val organizerId: UUID,
    val organizationId: UUID?,
    val category: EventCategoryModel,
    val address: String,
    val route: String,
    val description: String,
    val price: Double,
    val isOpen: Boolean,
    val status: EventStatusModel,
    val city: String,
    val isWithRegister: Boolean,
    val peopleLimit: Int?,
    val registerEndDateTime: Instant?,
)