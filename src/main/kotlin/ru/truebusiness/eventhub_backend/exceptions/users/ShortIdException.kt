package ru.truebusiness.eventhub_backend.exceptions.users

class ShortIdException private constructor(
    message: String
) : UsersException(message) {
    companion object {
        fun taken(shortId: String): ShortIdException {
            return ShortIdException("Short id $shortId is taken")
        }
    }
}
