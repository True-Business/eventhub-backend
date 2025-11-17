package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.FriendRequest
import ru.truebusiness.eventhub_backend.repository.entity.User
import java.util.UUID

@Repository
interface FriendRepository : JpaRepository<FriendRequest, UUID>, JpaSpecificationExecutor<FriendRequest> {
    fun existsBySenderAndReceiver(sender: User, receiver: User): Boolean
}