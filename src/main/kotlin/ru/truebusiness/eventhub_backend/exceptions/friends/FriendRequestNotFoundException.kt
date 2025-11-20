package ru.truebusiness.eventhub_backend.exceptions.friends

import java.util.UUID


class FriendRequestNotFoundException private constructor(
    message: String
) : FriendRequestException(message) {
    companion object {
        fun withId(requestId: UUID): FriendRequestNotFoundException {
            return FriendRequestNotFoundException(
                "Friend request '${requestId}' was not found."
            )
        }
    }
}