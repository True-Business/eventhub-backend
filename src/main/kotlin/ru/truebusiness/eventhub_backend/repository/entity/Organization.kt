package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn
import java.util.UUID

@Entity
@Table(name = "organizations")
class Organization(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    var name: String,
    @Column(nullable = false)
    var description: String,

    var address: String?,
    var pictureUrl: String?,

    @Column(nullable = false)
    val isVerified: Boolean = false,
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: User
)