package ru.truebusiness.eventhub_backend.conrollers.events.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.UUID
import ru.truebusiness.eventhub_backend.repository.entity.EventCategory
import ru.truebusiness.eventhub_backend.repository.entity.EventStatus

data class UpdateEventRequestDto(
    @param:Schema(
        description = "Новое название события",
        example = "Конференция по Kotlin 2025",
        nullable = true
    )
    val name: String?,
    @param:Schema(
        description = "Новая дата начала события",
        type = "string",
        format = "date-time",
        example = "2025-04-05T10:00:00Z",
        nullable = true
    )
    val startDateTime: LocalDateTime?,
    @param:Schema(
        description = "Новая дата окончания события",
        type = "string",
        format = "date-time",
        example = "2025-04-05T17:00:00Z",
        nullable = true
    )
    val endDateTime: LocalDateTime?,
    /**
     * Возможно organizerId и organizationId имеет смысл перенести в хэдэры запроса и уже
     * оттуда доставать их
     */
    @param:Schema(
        description = "Идентификатор организатора события (временно в теле запроса)",
        example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
        format = "uuid",
        nullable = true
    )
    val organizerId: UUID?,
    @param:Schema(
        description = "Идентификатор организации (временно в теле запроса)",
        example = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        format = "uuid",
        nullable = true
    )
    val organizationId: UUID?,
    @param:Schema(
        description = "Новая категория события",
        implementation = EventCategory::class,
        example = "PLACEHOLDER",
        nullable = true
    )
    val eventCategory: EventCategory?,
    @param:Schema(
        description = "Новый адрес проведения",
        example = "ул. Тверская, д. 10, Москва",
        nullable = true
    )
    val address: String?,
    @param:Schema(
        description = "Новые инструкции по проезду",
        example = "Метро Охотный ряд, выход 3",
        nullable = true
    )
    val route: String?,
    @param:Schema(
        description = "Новое описание события",
        example = "Обновленное описание конференции",
        nullable = true
    )
    val description: String?,
    @param:Schema(
        description = "Бесплатное ли событие",
        example = "false",
        nullable = true
    )
    val isFree: Boolean?,
    @param:Schema(
        description = "Новая стоимость участия",
        type = "number",
        format = "double",
        example = "750.0",
        minimum = "0",
        nullable = true
    )
    val price: Double?,
    @param:Schema(
        description = "Открытое ли событие",
        example = "true",
        nullable = true
    )
    val isOpen: Boolean?,
    @param:Schema(
        description = "Новый статус события",
        implementation = EventStatus::class,
        nullable = true
    )
    val eventStatus: String?,
    @param:Schema(
        description = "Новый город проведения",
        example = "Москва",
        nullable = true
    )
    val city: String?,
    @param:Schema(
        description = "Требуется ли регистрация",
        example = "true",
        nullable = true
    )
    val isWithRegister: Boolean?,
    @param:Schema(
        description = "Новое ограничение по количеству участников",
        example = "150",
        nullable = true
    )
    val peopleLimit: Int?,
    @param:Schema(
        description = "Новая дата окончания регистрации",
        type = "string",
        format = "date-time",
        example = "2025-03-25T23:59:59Z",
        nullable = true
    )
    val registerEndDateTime: LocalDateTime?,
)