package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "event_participants")
class EventParticipant(

    @Id
    @Column(name = "id")
    var id: UUID = UUID.randomUUID(),

    @Column(name = "user_id")
    var userId: UUID,

    @Column(name = "event_id")
    var eventId: UUID,

    @Column(name = "registered_at")
    var registeredAt: Instant = Instant.now()
)