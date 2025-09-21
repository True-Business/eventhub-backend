package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "organizations")
class Organization {
    @Id
    var id: UUID = UUID.randomUUID()

    var name: String = ""

    var description: String = ""

    var address: String = ""

    var isVerified: Boolean = false

    @ManyToOne
    @JoinColumn(name = "creator_id")
    var creator: User? = null
}