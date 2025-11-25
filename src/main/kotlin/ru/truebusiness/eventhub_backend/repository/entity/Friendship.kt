package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.*
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestStatus
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "friendship")
class Friendship (
    @Id
    @Column(nullable = false, unique = true)
    var id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    var user1: User,
    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    var user2: User,

    @Column(nullable = false)
    var since: Instant = Instant.now()
)