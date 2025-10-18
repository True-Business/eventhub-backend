package ru.truebusiness.eventhub_backend.conrollers.dto.events

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID
import ru.truebusiness.eventhub_backend.repository.entity.EventCategory
import ru.truebusiness.eventhub_backend.repository.entity.EventStatus

data class CreateEventRequestDto(
    @param:Schema(
        description = "Название события",
        example = "Конференция по Kotlin",
    )
    val name: String,

    @param:Schema(
        description = "Дата и время начала события",
        type = "string",
        format = "date-time",
        example = "2025-04-05T10:00:00Z",
    )
    val startDateTime: Instant,

    @param:Schema(
        description = "Дата и время окончания события (может быть null, если неизвестно)",
        type = "string",
        format = "date-time",
        example = "2025-04-05T17:00:00Z"
    )
    val endDateTime: Instant?,

    @param:Schema(
        description = "Идентификатор организатора события",
        example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
    )
    val organizerId: UUID,

    @param:Schema(
        description = "Идентификатор организации, связанной с событием (необязательно)",
        example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
    )
    val organizationId: UUID?,

    @param:Schema(
        description = "Категория события",
        implementation = EventCategory::class,
        example = "TECHNOLOGY",
    )
    val category: EventCategory,

    @param:Schema(
        description = "Физический адрес проведения события",
        example = "ул. Ленина, д. 15, Москва",
    )
    val address: String,

    @param:Schema(
        description = "Маршрут или инструкции по проезду (опционально)",
        example = "Вход со двора, подъезд 3"
    )
    val route: String,

    @param:Schema(
        description = "Описание события",
        example = "Ежегодная конференция разработчиков на Kotlin. Будут доклады, мастер-классы и нетворкинг.",
    )
    val description: String,

    @param:Schema(
        description = "Стоимость участия в событии (в условных единицах)",
        type = "number",
        format = "double",
        example = "500.0",
    )
    val price: Double,

    @param:Schema(
        description = "Признак, является ли событие открытым для всех",
        example = "true",
    )
    val isOpen: Boolean,

    @param:Schema(
        description = "Текущий статус события",
        implementation = EventStatus::class,
    )
    val status: EventStatus,

    @param:Schema(
        description = "Город проведения события",
        example = "Москва",
    )
    val city: String,

    @param:Schema(
        description = "Признак необходимости регистрации на событие",
        example = "true",
    )
    val isWithRegister: Boolean,

    @param:Schema(
        description = "Ограничение по количеству участников",
        example = "100",
    )
    val peopleLimit: Int?,

    @param:Schema(
        description = "Дата и время окончания регистрации на событие",
        type = "string",
        format = "date-time",
        example = "2025-04-01T23:59:59Z",
    )
    val registerEndDateTime: Instant?,
)