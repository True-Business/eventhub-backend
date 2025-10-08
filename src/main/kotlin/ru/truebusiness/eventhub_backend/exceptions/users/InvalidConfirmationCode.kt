package ru.truebusiness.eventhub_backend.exceptions.users;

class InvalidConfirmationCode private constructor(
    message: String
) : UsersException(message) {
    companion object {
        fun invalid(code: String): InvalidConfirmationCode {
            return InvalidConfirmationCode("Confirmation code: $code invalid")
        }

        fun expired(code: String): InvalidConfirmationCode {
            return InvalidConfirmationCode("Confirmation code: $code expired")
        }
    }
}
