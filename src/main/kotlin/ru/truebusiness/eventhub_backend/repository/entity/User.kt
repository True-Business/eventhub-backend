package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.Column
import java.time.Instant
import java.util.UUID


@Entity
@Table(name = "users")
class User {
    @Id
    var id: UUID = UUID.randomUUID()

    @Column(nullable = false)
    var username: String? = null

    @Column(nullable = false)
    var shortId: String? = null

    @Column(nullable = false)
    var registrationDate: Instant = Instant.now()

    @Column(nullable = false)
    var isConfirmed: Boolean? = null

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var credentials: UserCredentials? = null
}