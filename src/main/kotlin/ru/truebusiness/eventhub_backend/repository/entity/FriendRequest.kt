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
    var id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    var sender: User,
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    var receiver: User,

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    var status: FriendRequestStatus,

    @Column(nullable = false)
    var createdAt: Instant = Instant.now(),
    var acceptedAt: Instant? = null,
    )