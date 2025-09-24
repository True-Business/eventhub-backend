package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn
import java.util.UUID

@Entity
@Table(name = "organizations")
class Organization(
    @Id
    var id: UUID = UUID.randomUUID(),

    var name: String,
    var description: String,
    var address: String,
    var isVerified: Boolean,

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    var creator: User
)