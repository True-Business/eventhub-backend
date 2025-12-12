package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.domain.Specification
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestStatus
import ru.truebusiness.eventhub_backend.repository.entity.FriendRequest
import ru.truebusiness.eventhub_backend.repository.entity.User

object FriendRequestSpecs {
    fun withReceiver(receiver: User?): Specification<FriendRequest> =
        Specification { root, _, criteriaBuilder ->
            if (receiver != null) {
                criteriaBuilder.equal(root.get<User>("receiver"), receiver)
            } else null
        }

    fun withSender(sender: User?): Specification<FriendRequest> =
        Specification { root, _, criteriaBuilder ->
            if (sender != null) {
                criteriaBuilder.equal(root.get<User>("sender"), sender)
            } else null
        }

    fun withStatus(status: FriendRequestStatus?, invert: Boolean = false): Specification<FriendRequest> =
        Specification { root, _, criteriaBuilder ->
            if (status != null) {
                val predicate = criteriaBuilder.equal(root.get<FriendRequestStatus>("status"), status)
                if (invert) criteriaBuilder.not(predicate) else predicate
            } else null
        }
}