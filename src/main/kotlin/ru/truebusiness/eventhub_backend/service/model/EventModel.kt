package ru.truebusiness.eventhub_backend.service.model

import ru.truebusiness.eventhub_backend.repository.entity.EventCategory
import ru.truebusiness.eventhub_backend.repository.entity.EventStatus
import java.time.Instant
import java.util.*

class EventModel {
    var name: String = ""

    var startDateTime: Instant = Instant.now()
    var endDateTime: Instant? = null

    var updatedAt: Instant = Instant.now()

    var organizerId: UUID? = null
    var organizationId: UUID? = null
    var eventCategory: EventCategory = EventCategory.PLACEHOLDER

    var address: String = ""
    var route: String = ""
    var description: String = ""

    var isFree: Boolean = false
    var price: Double = 0.0
    var isOpen: Boolean = false

    var eventStatus: EventStatus = EventStatus.DRAFT
    var city: String = ""
    var isWithRegister: Boolean = false
    var peopleLimit: Int? = null

    var registerEndDateTime: Instant? = null
}