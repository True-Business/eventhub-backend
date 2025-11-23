package ru.truebusiness.eventhub_backend.conrollers.dto.storage

import java.util.UUID

object ObjectDownload {
    data class Request(
        var fileId: UUID
    )

    data class Response(
        var fileId: UUID,
        var downloadUrl: String,
    )
}
