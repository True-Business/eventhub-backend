package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType
import jakarta.persistence.Column
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "events")
class Event {

    @Id
    var id: UUID = UUID.randomUUID()

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false)
    var startDateTime: Instant? = null

    var endDateTime: Instant? = null

    @Column(nullable = false)
    var updatedAt: Instant = Instant.now()

    @Column(nullable = false)
    var organizerId: UUID? = null

    var organizationId: UUID? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var category: EventCategory? = null

    @Column(nullable = false)
    var address: String? = null

    @Column(nullable = false)
    var route: String? = null

    @Column(nullable = false)
    var description: String? = null

    @Column(nullable = false)
    var price: Double? = null

    @Column(nullable = false)
    var isOpen: Boolean? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: EventStatus? = null

    @Column(nullable = false)
    var city: String? = null

    @Column(nullable = false)
    var isWithRegister: Boolean? = null

    var peopleLimit: Int? = null

    var registerEndDateTime: Instant? = null
}