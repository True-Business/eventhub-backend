package ru.truebusiness.eventhub_backend.conrollers.dto.friends

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID

data class FriendRequestDto (
    @param:Schema(
        description = "ID запроса",
        format = "uuid",
        example = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    )
    val id: UUID,
    @param:Schema(
        description = "ID отправителя",
        format = "uuid",
        example = "e1800733-370c-4b23-8f01-910477248c95",
    )
    val sender: UUID,
    @param:Schema(
        description = "ID получателя",
        format = "uuid",
        example = "5d3ae284-5a4d-41f0-ae35-65b39af25b4e",
    )
    val receiver: UUID,
    @param:Schema(
        description = "Состояние запроса",
        example = "PENDING",
    )
    val status: String,
    @param:Schema(
        description = "Дата создания запроса",
        format = "date-time",
        example = "2025-04-05T10:00:00Z",
    )
    val createdAt: Instant,
    @param:Schema(
        description = "Дата принятия запроса (null, если не принят)",
        format = "date-time",
        example = "2025-04-05T10:00:00Z",
    )
    val acceptedAt: Instant?
)