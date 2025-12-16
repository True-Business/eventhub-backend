package ru.truebusiness.eventhub_backend.exceptions.users

class CredentialsException private constructor(
    message: String
) : UsersException(message) {
    companion object {
        fun invalid(): CredentialsException {
            return CredentialsException("Invalid credentials")
        }
    }
}
