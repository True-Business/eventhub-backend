package ru.truebusiness.eventhub_backend.conrollers.dto

import ru.truebusiness.eventhub_backend.service.model.EventStatusModel
import java.time.Instant
import java.util.UUID

data class EventSearchFilter(
    val city: String?,
    val minPrice: Double?,
    val maxPrice: Double?,
    val startDateTime: Instant?,
    val minDurationMinutes: Int?,
    val maxDurationMinutes: Int?,
    val organizerId: UUID?,
    val isParticipant: Boolean?,
    val status: EventStatusModel?,
    val isOpen: Boolean?,
)
/*
  "city": null,
  "minPrice": int,
  "maxPrice": int,
  "startDateTime": now,
  "duration": null,
  "organizerId": null,
  "isParticipant": null,
  "status": "PLANNED",
  "isOpen": null
 */