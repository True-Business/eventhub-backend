package ru.truebusiness.eventhub_backend.conrollers.dto

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class CreateEventRequestDto(
    val name: String,
    val startDate: LocalDateTime?,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val organizerId: UUID?,
    val organizationId: UUID?,
    val eventCategory: Int?,
    val address: String?,
    val route: String?,
    val description: String?,
    val isFree: Boolean?,
    val price: Double?,
    val isOpen: Boolean?,
    val eventStatus: String?,
    val city: String?,
    val isWithRegister: Boolean?,
    val peopleLimit: Int?,
    val registerEndDate: LocalDate?,
    val registerEndTime: LocalDateTime?
)
