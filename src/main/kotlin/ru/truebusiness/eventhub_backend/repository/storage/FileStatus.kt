package ru.truebusiness.eventhub_backend.repository.storage

enum class FileStatus(val prefix: String) {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    DELETED("");
}