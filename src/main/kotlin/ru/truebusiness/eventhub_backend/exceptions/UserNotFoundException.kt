package ru.truebusiness.eventhub_backend.exceptions

class UserNotFoundException(message: String, cause: Throwable?) : RuntimeException(message, cause)