package ru.truebusiness.eventhub_backend.service.model

import java.util.*

class CreateFriendRequestModel (
    var sender: UUID,
    var receiver: UUID
)