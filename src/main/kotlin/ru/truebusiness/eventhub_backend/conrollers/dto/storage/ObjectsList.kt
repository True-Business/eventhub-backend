package ru.truebusiness.eventhub_backend.conrollers.dto.storage

import java.util.UUID
import ru.truebusiness.eventhub_backend.conrollers.dto.Page

object ObjectsList {
    data class Request(
        val ownerId: UUID,
        val ownerType: String,
        val page: Page? = null,
    )

    data class Response(
        val objects: List<Object>,
    )

    data class Object(
        val id: UUID,
        val origin: String,
    )
}
