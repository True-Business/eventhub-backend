package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
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
    @Column(nullable = false)
    val creatorId: UUID
)