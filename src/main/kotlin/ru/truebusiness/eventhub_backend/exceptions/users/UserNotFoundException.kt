package ru.truebusiness.eventhub_backend.exceptions.users

import java.util.UUID


class UserNotFoundException private constructor(
    message: String
) : UsersException(message) {
    companion object {
        fun withId(id: UUID): UserNotFoundException {
            return UserNotFoundException("User with id: $id not found")
        }

        fun withEmail(email: String): UserNotFoundException {
            return UserNotFoundException("User with email: $email not found")
        }

        fun withShortId(shortId: String): UserNotFoundException {
            return UserNotFoundException("User with shortId: $shortId not found")
        }
    }
}
