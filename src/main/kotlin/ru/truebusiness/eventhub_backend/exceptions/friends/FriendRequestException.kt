package ru.truebusiness.eventhub_backend.exceptions.friends

open class FriendRequestException (
    override val message: String
) : RuntimeException(message)