package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.*
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestStatus
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "friend_requests")
class FriendRequest (
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID = UUID.randomUUID(),

    @JoinColumn(nullable = false)
    var sender: User,
    @JoinColumn(nullable = false)
    var receiver: User,

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    var status: FriendRequestStatus,

    @Column(nullable = false)
    var createdAt: Instant = Instant.now(),
    var acceptedAt: Instant? = null,
    )