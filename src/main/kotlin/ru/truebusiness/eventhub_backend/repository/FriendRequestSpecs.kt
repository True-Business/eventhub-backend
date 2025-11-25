package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.domain.Specification
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestStatus
import ru.truebusiness.eventhub_backend.repository.entity.FriendRequest
import ru.truebusiness.eventhub_backend.repository.entity.User
import java.util.UUID

object FriendRequestSpecs {
    fun withSender(sender: User?): Specification<FriendRequest> =
        Specification { root, _, criteriaBuilder ->
            if (sender != null) {
                criteriaBuilder.equal(root.get<User>("sender"), sender)
            } else null
        }

    fun withoutStatus(status: FriendRequestStatus?): Specification<FriendRequest> =
        Specification { root, _, criteriaBuilder ->
            if (status != null) {
                criteriaBuilder.notEqual(root.get<FriendRequestStatus>("status"), status)
            } else null
        }
}