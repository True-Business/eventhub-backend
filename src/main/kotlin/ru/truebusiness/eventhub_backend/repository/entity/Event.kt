package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "events")
class Event(
    @Id
    var id: UUID = UUID.randomUUID(),

    var name: String,
    var startDateTime: Instant,
    var endDateTime: Instant?,
    var updatedAt: Instant = Instant.now(),
    var organizerId: UUID,
    var organizationId: UUID?,

    @Enumerated(EnumType.STRING)
    var category: EventCategory,
    var address: String,
    var route: String,
    var description: String,
    var price: Double,
    var isOpen: Boolean,

    @Enumerated(EnumType.STRING)
    var status: EventStatus,
    var city: String,
    var isWithRegister: Boolean,
    var peopleLimit: Int?,
    var registerEndDateTime: Instant?
)