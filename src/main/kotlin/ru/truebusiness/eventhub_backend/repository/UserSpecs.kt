package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.domain.Specification
import ru.truebusiness.eventhub_backend.repository.entity.Friendship
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

    fun isFriendOf(userFriend: User?): Specification<User> =
        Specification { root, query, criteriaBuilder ->
            if (userFriend != null) {
                val subquery = query!!.subquery(Long::class.javaObjectType)
                val friendshipRoot = subquery.from(Friendship::class.java)

                // Условие проверки дружбы в обе стороны
                val friendshipCondition = criteriaBuilder.or(
                    criteriaBuilder.and(
                        criteriaBuilder.equal(friendshipRoot.get<User>("user1"), userFriend),
                        criteriaBuilder.equal(friendshipRoot.get<User>("user2"), root)
                    ),
                    criteriaBuilder.and(
                        criteriaBuilder.equal(friendshipRoot.get<User>("user1"), root),
                        criteriaBuilder.equal(friendshipRoot.get<User>("user2"), userFriend)
                    )
                )


                subquery.select(criteriaBuilder.literal(1L))
                subquery.where(friendshipCondition)

                criteriaBuilder.exists(subquery)
            } else criteriaBuilder.disjunction()
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