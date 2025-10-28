package ru.truebusiness.eventhub_backend.conrollers.dto

import java.util.UUID

data class FindUsersRequestDto(
    val username: String?,
    val userIdFriend: UUID?,
    val userIdRequestTo: UUID?,
    val userIdRequestFrom: UUID?,
    val eventIdParticipant: UUID?,
    val organizationIdAdmin: UUID?
)