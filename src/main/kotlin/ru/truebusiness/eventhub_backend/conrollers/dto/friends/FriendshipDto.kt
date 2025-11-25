package ru.truebusiness.eventhub_backend.conrollers.dto.friends

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID

data class FriendshipDto (
    @param:Schema(
        description = "ID записи",
        format = "uuid",
        example = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    )
    val id: UUID,
    @param:Schema(
        description = "ID пользователя 1",
        format = "uuid",
        example = "e1800733-370c-4b23-8f01-910477248c95",
    )
    val user1: UUID,
    @param:Schema(
        description = "ID пользователя 2",
        format = "uuid",
        example = "5d3ae284-5a4d-41f0-ae35-65b39af25b4e",
    )
    val user2: UUID,
    @param:Schema(
        description = "Дата начала дружбы",
        format = "date-time",
        example = "2025-04-05T10:00:00Z",
    )
    val since: Instant
)