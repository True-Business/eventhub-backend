package ru.truebusiness.eventhub_backend.service.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class EventModel {
    var name: String = ""

    var startDate: LocalDateTime? = null
    var startTime: LocalDateTime? = null
    var endTime: LocalDateTime? = null

    var updatedAt: LocalDateTime? = LocalDateTime.now()

    var organizerId: UUID? = null
    var organizationId: UUID? = null
    var eventCategory: Int? = null

    var address: String? = null
    var route: String? = null
    var description: String? = null

    var isFree: Boolean? = null
    var price: Double? = null
    var isOpen: Boolean? = null

    var eventStatus: String? = null
    var city: String? = null
    var isWithRegister: Boolean? = null
    var peopleLimit: Int? = null

    var registerEndDate: LocalDate? = null
    var registerEndTime: LocalDateTime? = null
}