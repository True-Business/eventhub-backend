package ru.truebusiness.eventhub_backend.exceptions.users

class UserAlreadyExistsException private constructor(
    message: String
) : UsersException(message) {
    companion object {
        fun withShortId(shortId: String): UserAlreadyExistsException {
            return UserAlreadyExistsException("user with shortId: $shortId already exists")
        }

        fun withEmail(email: String): UserAlreadyExistsException {
            return UserAlreadyExistsException("user with email: $email already exists")
        }
    }
}