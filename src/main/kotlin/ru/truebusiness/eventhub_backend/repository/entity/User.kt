package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var username: String?,
    @Column(nullable = false)
    var shortId: String?,
    var bio: String,
    @Column(nullable = false)
    var registrationDate: Instant = Instant.now(),
    @Column(nullable = false)
    var isConfirmed: Boolean?,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var credentials: UserCredentials?,

    @ManyToMany(mappedBy = "participants")
    var events: MutableList<Event> = mutableListOf()
)