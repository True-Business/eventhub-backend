package ru.truebusiness.eventhub_backend.exceptions.users

open class UsersException(
    override val message: String
) : RuntimeException(message)
