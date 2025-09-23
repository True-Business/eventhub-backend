package ru.truebusiness.eventhub_backend.service.model

import ru.truebusiness.eventhub_backend.service.model.EventCategoryModel
import ru.truebusiness.eventhub_backend.service.model.EventStatusModel
import java.time.Instant
import java.util.UUID

class EventModel {
    var name: String = ""

    var startDateTime: Instant = Instant.now()
    var endDateTime: Instant? = null

    var updatedAt: Instant = Instant.now()

    var organizerId: UUID? = null
    var organizationId: UUID? = null
    var category: EventCategoryModel = EventCategoryModel.PLACEHOLDER

    var address: String = ""
    var route: String = ""
    var description: String = ""

    var price: Double = 0.0
    var isOpen: Boolean = false

    var status: EventStatusModel = EventStatusModel.DRAFT
    var city: String = ""
    var isWithRegister: Boolean = false
    var peopleLimit: Int? = null

    var registerEndDateTime: Instant? = null
}