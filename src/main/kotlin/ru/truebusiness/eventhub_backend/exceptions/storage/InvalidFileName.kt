package ru.truebusiness.eventhub_backend.exceptions.storage

class InvalidFileName private constructor(
    message: String
) : ObjectStorageException(message) {
    companion object {
        fun byTargetPattern(pattern: String): InvalidFileName {
            return InvalidFileName("target pattern $pattern")
        }
    }
}