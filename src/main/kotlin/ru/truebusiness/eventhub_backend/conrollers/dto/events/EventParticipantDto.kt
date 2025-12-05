package ru.truebusiness.eventhub_backend.conrollers.dto.events

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID

data class EventParticipantDto(
    @param:Schema(
        description = "Идентификатор записи",
        example = "ca7ef39b-01da-1982-1j90-0a3190bba54c",
    )
    val id: UUID,

    @param:Schema(
        description = "Идентификатор участника события",
        example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
    )
    val userId: UUID,

    @param:Schema(
        description = "Идентификатор события",
        example = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    )
    val eventId: UUID,

    @param:Schema(
        description = "Дата и время регистрации на событие",
        type = "string",
        format = "date-time",
        example = "2025-04-01T23:59:59Z",
    )
    val registeredAt: Instant
)
