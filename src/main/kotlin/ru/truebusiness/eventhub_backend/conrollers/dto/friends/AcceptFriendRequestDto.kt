package ru.truebusiness.eventhub_backend.conrollers.dto.friends

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

data class AcceptFriendRequestDto (
    @param:Schema(
        description = "ID запроса, который требуется принять",
        format = "uuid",
        example = "e1800733-370c-4b23-8f01-910477248c95",
    )
    val requestId: UUID
)
