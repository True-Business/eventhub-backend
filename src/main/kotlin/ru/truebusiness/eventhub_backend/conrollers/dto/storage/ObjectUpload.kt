package ru.truebusiness.eventhub_backend.conrollers.dto.storage

import java.util.UUID

object ObjectUpload {
    data class Request(
        val ownerId: UUID,
        val ownerType: String,
        val originNames: List<String>,
    )

    data class Response(
        val urls: List<UrlInfo>
    )

    data class UrlInfo(
        val origin: String,
        val id: UUID,
        val url: String?,
    )
}