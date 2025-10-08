package ru.truebusiness.eventhub_backend.conrollers.events.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID
import ru.truebusiness.eventhub_backend.repository.entity.EventCategory
import ru.truebusiness.eventhub_backend.repository.entity.EventStatus

data class EventDto(
    @param:Schema(
        description = "ID события",
        example = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    )
    val id: UUID,

    @param:Schema(
        description = "Название события",
        example = "Конференция по Kotlin 2025",
    )
    val name: String,

    @param:Schema(
        description = "Дата начала события",
        format = "date-time",
        example = "2025-04-05T10:00:00Z",
    )
    val startDateTime: Instant,

    @param:Schema(
        description = "Дата окончания события",
        format = "date-time",
        example = "2025-04-05T17:00:00Z",
    )
    val endDateTime: Instant?,

    @param:Schema(
        description = "Время последнего изменения события",
        format = "date-time",
        example = "2025-04-05T17:00:00Z",
    )
    val updatedAt: Instant,

    @param:Schema(
        description = "Идентификатор организатора события (временно в теле запроса)",
        example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
        format = "uuid",
    )
    val organizerId: UUID,

    @param:Schema(
        description = "Идентификатор организации (временно в теле запроса)",
        example = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        format = "uuid",
    )
    val organizationId: UUID?,

    @param:Schema(
        description = "Новая категория события",
        implementation = EventCategory::class,
    )
    val category: EventCategory,

    @param:Schema(
        description = "Новый адрес проведения",
        example = "ул. Тверская, д. 10, Москва",
    )
    val address: String,

    @param:Schema(
        description = "Новые инструкции по проезду",
        example = "Метро Охотный ряд, выход 3",
    )
    val route: String,

    @param:Schema(
        description = "Новое описание события",
        example = "Обновленное описание конференции",
    )
    val description: String,

    @param:Schema(
        description = "Новая стоимость участия",
        type = "number",
        format = "double",
        example = "750.0",
        minimum = "0",
        nullable = true
    )
    val price: Double,

    @param:Schema(
        description = "Открытое ли событие",
        example = "true",
    )
    val isOpen: Boolean,

    @param:Schema(
        description = "Новый статус события",
        implementation = EventStatus::class,
    )
    val status: EventStatus,

    @param:Schema(
        description = "Новый город проведения",
        example = "Москва",
    )
    val city: String,

    @param:Schema(
        description = "Требуется ли регистрация",
        example = "true",
    )
    val isWithRegister: Boolean,

    @param:Schema(
        description = "Новое ограничение по количеству участников",
        example = "150",
    )
    val peopleLimit: Int?,

    @param:Schema(
        description = "Новая дата окончания регистрации",
        type = "string",
        format = "date-time",
        example = "2025-03-25T23:59:59Z",
    )
    val registerEndDateTime: Instant?,
)