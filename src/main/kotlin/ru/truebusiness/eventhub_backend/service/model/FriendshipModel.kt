package ru.truebusiness.eventhub_backend.service.model

import java.time.Instant
import java.util.UUID

class FriendshipModel (
    var id: UUID,
    var user1Id: UUID,
    var user2Id: UUID,
    var since: Instant
)