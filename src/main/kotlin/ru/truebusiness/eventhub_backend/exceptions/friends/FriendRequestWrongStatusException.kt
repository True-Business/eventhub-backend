package ru.truebusiness.eventhub_backend.exceptions.friends

import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestStatus


class FriendRequestWrongStatusException private constructor(
    message: String
) : FriendRequestException(message) {
    companion object {
        fun withStatus(status: FriendRequestStatus): FriendRequestWrongStatusException {
            return FriendRequestWrongStatusException(
                "Friend request has wrong status: '${status.name}'."
            )
        }
    }
}