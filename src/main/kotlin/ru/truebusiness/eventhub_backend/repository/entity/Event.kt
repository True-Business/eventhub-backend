package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

enum class EventCategory {
    PLACEHOLDER
}

enum class EventStatus {
    DRAFT,
    PLANNED,
    ENDED,
    CANCELED
}

@Entity
@Table(name = "events")
class Event {

    @Id
    var id: UUID = UUID.randomUUID()

    var name: String = ""

    var startDateTime: Instant = Instant.now()

    var endDateTime: Instant? = null

    var updatedAt: Instant = Instant.now()

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    var organizer: User? = null

    @ManyToOne
    @JoinColumn(name = "organization_id")
    var organization: Organization? = null

    @Enumerated(EnumType.STRING)
    var eventCategory: EventCategory = EventCategory.PLACEHOLDER

    var address: String = ""
    var route: String = ""
    var description: String = ""

    var isFree: Boolean = false
    var price: Double = 0.0
    var isOpen: Boolean = false

    @Enumerated(EnumType.STRING)
    var eventStatus: EventStatus = EventStatus.DRAFT

    var city: String = ""
    var isWithRegister: Boolean = false
    var peopleLimit: Int? = null

    var registerEndDateTime: Instant? = null
}