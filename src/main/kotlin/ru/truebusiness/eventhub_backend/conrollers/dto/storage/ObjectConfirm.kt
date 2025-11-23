package ru.truebusiness.eventhub_backend.conrollers.dto.storage

import java.util.UUID

object ObjectConfirm {
    data class Request(
        val ids: List<UUID>,
        val ownerId: UUID,
        val ownerType: UUID
    )

    data class Response(
        val fileId: String, val status: FileConfirm
    )

    enum class FileConfirm(
        val status: String
    ) {
        UPLOADED("uploaded"),
        EXPIRED("expired"),
        NOT_FOUND("not_found");
    }
}