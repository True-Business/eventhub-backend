package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "confirmation_codes")
class ConfirmationCode(
    @Id
    var id: UUID = UUID.randomUUID(),
    var code: String,
    var expiresAt: Instant,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User
)