package ru.truebusiness.eventhub_backend.service.model

import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestStatus
import java.time.Instant
import java.util.UUID

class FriendRequestModel (
    var id: UUID,
    var senderId: UUID,
    var receiverId: UUID,
    var status: FriendRequestStatus,
    var createdAt: Instant,
    var acceptedAt: Instant?
)