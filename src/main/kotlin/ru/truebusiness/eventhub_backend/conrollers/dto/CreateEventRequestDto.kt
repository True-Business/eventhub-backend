package ru.truebusiness.eventhub_backend.conrollers.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class CreateEventRequestDto(
    @field:Schema(description = "Название мероприятия", required = true)
    val name: String,

    @field:Schema(
        description = "Дата начала мероприятия (формат: yyyy-MM-dd)",
        nullable = true,
    )
    val startDate: LocalDateTime?,

    @field:Schema(
        description = "Время начала мероприятия (формат: HH:mm:ss)",
        nullable = true,
    )
    val startTime: LocalDateTime?,

    @field:Schema(
        description = "Время окончания мероприятия (формат: HH:mm:ss)",
        nullable = true,
    )
    val endTime: LocalDateTime?,
    /**
     * Возможно organizerId и organizationId имеет смысл перенести в хэдэры запроса и уже
     * оттуда доставать их
     */
    @field:Schema(
        description = "ID организатора",
        example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
        nullable = true,
    )
    val organizerId: UUID?,

    @field:Schema(
        description = "ID организации",
        example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
        nullable = true,
    )
    val organizationId: UUID?,

    @field:Schema(
        description = "Категория мероприятия (целочисленный идентификатор)",
        example = "1",
        nullable = true,
    )
    val eventCategory: Int?,

    @field:Schema(
        description = "Адрес проведения мероприятия",
        example = "ул. Ленина, д. 10",
        nullable = true,
    )
    val address: String?,

    @field:Schema(
        description = "Маршрут до места проведения",
        example = "https://yandex.ru/maps/...",
        nullable = true,
    )
    val route: String?,

    @field:Schema(
        description = "Описание мероприятия",
        nullable = true,
    )
    val description: String?,

    @field:Schema(
        description = "Бесплатное мероприятие", example = "true",
        nullable = true,
    )
    val isFree: Boolean?,

    @field:Schema(
        description = "Стоимость участия", example = "999.99",
        nullable = true,
    )
    val price: Double?,

    @field:Schema(
        description = "Открытое мероприятие", example = "true",
        nullable = true,
    )
    val isOpen: Boolean?,

    @field:Schema(
        description = "Статус мероприятия", example = "PUBLISHED",
        nullable = true,
    )
    val eventStatus: String?,

    @field:Schema(
        description = "Город проведения", example = "Москва",
        nullable = true,
    )
    val city: String?,

    @field:Schema(
        description = "Требуется регистрация", example = "true",
        nullable = true,
    )
    val isWithRegister: Boolean?,

    @field:Schema(
        description = "Ограничение по количеству участников", example = "100",
        nullable = true,
    )
    val peopleLimit: Int?,

    @field:Schema(
        description = "Дата окончания регистрации (формат: yyyy-MM-dd)",
        nullable = true,
    )
    val registerEndDate: LocalDate?,

    @field:Schema(
        description = "Время окончания регистрации (формат: yyyy-MM-ddTHH:mm:ss)",
        nullable = true,
    )
    val registerEndTime: LocalDateTime?
)
