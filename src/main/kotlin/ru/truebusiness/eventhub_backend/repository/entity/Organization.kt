package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn
import jakarta.persistence.Column
import java.util.UUID

@Entity
@Table(name = "organizations")
class Organization {
    @Id
    var id: UUID = UUID.randomUUID()

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false)
    var description: String? = null

    @Column(nullable = false)
    var address: String? = null

    @Column(nullable = false)
    var isVerified: Boolean? = null

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    var creator: User? = null
}