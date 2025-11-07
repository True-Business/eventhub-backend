package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.domain.Specification
import ru.truebusiness.eventhub_backend.repository.entity.User
import java.util.UUID

object UserSpecs {
    fun withUsername(username: String?): Specification<User> =
        Specification { root, query, criteriaBuilder ->
            if (!username.isNullOrBlank()) {
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")), "%${username.lowercase()}%")
            } else null
        }

    fun isFriendOf(userIdFriend: UUID?): Specification<User> =
        Specification { root, query, criteriaBuilder ->
            null
        }

    fun hasFriendRequestTo(userIdRequestTo: UUID?): Specification<User> =
        Specification { root, query, criteriaBuilder ->
            null
        }

    fun hasFriendRequestFrom(userIdRequestFrom: UUID?): Specification<User> =
        Specification { root, query, criteriaBuilder ->
            null
        }

    fun isParticipantOf(eventIdParticipant: UUID?): Specification<User> =
        Specification { root, query, criteriaBuilder ->
            null
        }

    fun isAdminOf(organizationIdAdmin: UUID?): Specification<User> =
        Specification { root, query, criteriaBuilder ->
            null
        }
}