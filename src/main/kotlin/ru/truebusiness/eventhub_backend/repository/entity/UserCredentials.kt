package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.Column
import java.util.UUID

@Entity
@Table(name = "user_credentials")
class UserCredentials {
    @Id
    var id: UUID = UUID.randomUUID()

    @Column(nullable = false)
    var email: String? = null

    @Column(nullable = false)
    var password: String? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null
}