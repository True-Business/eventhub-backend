package ru.truebusiness.eventhub_backend.exceptions

class UserNotFoundException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)