package ru.truebusiness.eventhub_backend.conrollers.dto.posts

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID

data class PostDto(
    @param:Schema(
        description = "ID поста",
        example = "f168210b-abdc-0198-b321-1839203ccd13",
    )
    val id: UUID,

    @param:Schema(
        description = "ID связанного события",
        example = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    )
    val eventId: UUID,

    @param:Schema(
        description = "Дата создания поста",
        format = "date-time",
        example = "2025-04-05T10:00:00Z",
    )
    val createdAt: Instant,

    @param:Schema(
        description = "Дата редактировния поста",
        format = "date-time",
        example = "2025-04-06T10:00:00Z",
        nullable = true
    )
    val modifiedAt: Instant?,

    @param:Schema(
        description = "Содержание поста",
        example = "Всем привет (*^_^*)",
    )
    val text: String
)