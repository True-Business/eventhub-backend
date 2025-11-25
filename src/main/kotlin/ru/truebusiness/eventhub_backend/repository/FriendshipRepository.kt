package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.Friendship
import java.util.UUID

@Repository
interface FriendshipRepository : JpaRepository<Friendship, UUID>, JpaSpecificationExecutor<Friendship> {

}