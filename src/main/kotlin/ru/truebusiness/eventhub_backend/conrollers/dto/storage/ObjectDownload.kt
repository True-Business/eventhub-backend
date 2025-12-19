package ru.truebusiness.eventhub_backend.conrollers.dto.storage

import java.time.Instant
import java.util.UUID

object ObjectDownload {
    data class Request(
        var ids: List<UUID>
    )

    data class Response(
        var urls: List<UrlInfo>
    )

    data class UrlInfo(
        var id: UUID,
        var meta: Meta?,
    )

    data class Meta(
        var downloadUrl: String,
        var uploaded: Instant,
    )
}
