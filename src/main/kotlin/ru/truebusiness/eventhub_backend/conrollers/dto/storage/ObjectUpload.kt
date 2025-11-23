package ru.truebusiness.eventhub_backend.conrollers.dto.storage

import java.util.UUID

object ObjectUpload {
    data class Request(
        val ownerId: UUID,
        val ownerType: UUID,
        val originNames: List<String>,
    )

    data class Response(
        val origin: String,
        val id: UUID,
        val url: String,
    )
}