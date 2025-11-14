package ru.truebusiness.eventhub_backend.exceptions.friends

import java.util.UUID


class FriendRequestAlreadyExistsException private constructor(
    message: String
) : FriendRequestException(message) {
    companion object {
        fun withSenderAndReceiver(senderId: UUID, receiverId: UUID): FriendRequestAlreadyExistsException {
            return FriendRequestAlreadyExistsException(
                "Friend request from '${senderId}' to '${receiverId}' already exists"
            )
        }
    }
}