package ru.truebusiness.eventhub_backend.exceptions.storage

class FileNotFound(
    origin: String
) : ObjectStorageException("File $origin not found")