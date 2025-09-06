package ru.truebusiness.eventhub_backend.conrollers.dto

import java.util.*

data class NewEventResponse(
    val id: UUID?,
    val eventName: String?,
)