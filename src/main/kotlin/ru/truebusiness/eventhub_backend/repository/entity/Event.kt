package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "events")
class Event(

    @Id
    @Column(name = "id")
    var id: UUID = UUID.randomUUID(),

    @Column(name = "name")
    var name: String,

    @Column(name = "start_datetime")
    var startDateTime: Instant,

    @Column(name = "end_datetime")
    var endDateTime: Instant?,

    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now(),

    @Column(name = "organizer_id")
    var organizerId: UUID,

    @Column(name = "organization_id")
    var organizationId: UUID?,

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    var category: EventCategory,

    @Column(name = "address")
    var address: String,

    @Column(name = "route")
    var route: String,

    @Column(name = "description")
    var description: String,

    @Column(name = "price", precision = 10, scale = 2)
    var price: Double,

    @Column(name = "is_open")
    var isOpen: Boolean,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: EventStatus,

    @Column(name = "city")
    var city: String,

    @Column(name = "is_with_register")
    var isWithRegister: Boolean,

    @Column(name = "people_limit")
    var peopleLimit: Int?,

    @Column(name = "register_end_datetime")
    var registerEndDateTime: Instant?
)