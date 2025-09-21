package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID


@Entity
@Table(name = "users")
class User {
    @Id
    var id: UUID? = UUID.randomUUID()

    var username: String = ""

    var shortId: String = ""

    var registrationDate: Instant = Instant.now()

    var isConfirmed: Boolean = false

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var credentials: UserCredentials? = null
}