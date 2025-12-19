package ru.truebusiness.eventhub_backend.conrollers.dto.storage

import java.util.UUID
import ru.truebusiness.eventhub_backend.repository.storage.FileStatus

object ObjectConfirm {
    data class Request(
        val ownerId: UUID,
        val ownerType: String,
        val ids: List<UUID>,
    )

    data class Response(
        val statuses: List<ConfirmInfo>,
    )

    data class ConfirmInfo(
        val id: UUID,
        val status: FileConfirm,
    )

    enum class FileConfirm(
        val status: String
    ) {
        UPLOADED("uploaded"),
        NOT_FOUND("not_found");
    }
}