package ru.truebusiness.eventhub_backend.exceptions.friends

import java.util.UUID


class SelfFriendRequestException private constructor(
    message: String
) : FriendRequestException(message) {
    companion object {
        fun withId(senderId: UUID): SelfFriendRequestException {
            return SelfFriendRequestException(
                "Friend request from '${senderId}' was directed to themselves."
            )
        }
    }
}