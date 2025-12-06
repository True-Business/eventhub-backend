package ru.truebusiness.eventhub_backend.exceptions.storage

open class ObjectStorageException(
    override val message: String
) : RuntimeException(message)