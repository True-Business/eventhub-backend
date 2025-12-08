package ru.truebusiness.eventhub_backend.exceptions.friends

import java.util.UUID


class FriendRequestAlreadyExistsException private constructor(
    message: String
) : FriendRequestException(message) {
    companion object {
        fun withSenderAndReceiver(senderId: UUID, receiverId: UUID): FriendRequestAlreadyExistsException {
            return FriendRequestAlreadyExistsException(
                "Failed to create friend request between $senderId and $receiverId, one is already pending"
            )
        }
    }
}