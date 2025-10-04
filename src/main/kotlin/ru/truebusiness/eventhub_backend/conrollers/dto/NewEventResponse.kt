package ru.truebusiness.eventhub_backend.conrollers.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

data class NewEventResponse(
    @field:Schema(
        description = "Уникальный идентификатор мероприятия",
        example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
        nullable = true,
    )
    val id: UUID?,

    @field:Schema(
        description = "Название мероприятия",
        example = "Конференция разработчиков",
        nullable = true,
    )
    val eventName: String?,
)