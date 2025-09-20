package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "organizations")
class Organization {

    @Id
    var id: UUID = UUID.randomUUID()

    var name: String = ""
    var description: String = ""

    var address: String? = null
    var picture: String? = null

    @Column(name = "is_verified")
    val isVerified: Boolean? = null
    @Column(name = "creator_id")
    val creatorId: UUID? = null
}