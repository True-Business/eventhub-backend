package ru.truebusiness.eventhub_backend.conrollers.dto

import ru.truebusiness.eventhub_backend.repository.entity.EventCategory
import ru.truebusiness.eventhub_backend.repository.entity.EventStatus
import java.time.Instant
import java.util.UUID

data class CreateEventRequestDto(
    val name: String,
    val startDateTime: Instant,
    val endDateTime: Instant?,
    /**
     * Возможно organizerId и organizationId имеет смысл перенести в хэдэры запроса и уже
     * оттуда доставать их
     */
    val organizerId: UUID,
    val organizationId: UUID?,
    val category: EventCategory,
    val address: String,
    val route: String,
    val description: String,
    val price: Double,
    val isOpen: Boolean,
    val status: EventStatus,
    val city: String,
    val isWithRegister: Boolean,
    val peopleLimit: Int?,
    val registerEndDateTime: Instant?,
)
