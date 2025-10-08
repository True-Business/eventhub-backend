package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "user_credentials")
class UserCredentials(
    @Id
    var id: UUID = UUID.randomUUID(),
    @Column(unique = true)
    var email: String,
    var password: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User
)