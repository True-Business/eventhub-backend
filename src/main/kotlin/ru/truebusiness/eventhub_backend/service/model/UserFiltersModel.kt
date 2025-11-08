package ru.truebusiness.eventhub_backend.service.model

import java.util.UUID

class UserFiltersModel(
    val username: String?,
    val userIdFriend: UUID?,
    val userIdRequestTo: UUID?,
    val userIdRequestFrom: UUID?,
    val eventIdParticipant: UUID?,
    val organizationIdAdmin: UUID?
)