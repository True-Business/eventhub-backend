package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "posts")
class Post(
    @Id
    @Column(name = "id")
    var id: UUID = UUID.randomUUID(),

    @Column(name = "event_id")
    var eventId: UUID,

    @Column(name = "created_at")
    var createdAt: Instant = Instant.now(),

    @Column(name = "modified_at")
    var modifiedAt: Instant?,

    @Column(name = "text")
    var text: String
)