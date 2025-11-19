package ru.truebusiness.eventhub_backend.conrollers.dto.friends

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

data class CreateFriendRequestDto (
    @param:Schema(
        description = "ID отправителя",
        format = "uuid",
        example = "e1800733-370c-4b23-8f01-910477248c95",
    )
    val sender: UUID,
    @param:Schema(
        description = "ID получателя",
        format = "uuid",
        example = "5d3ae284-5a4d-41f0-ae35-65b39af25b4e",
    )
    val receiver: UUID,
)
