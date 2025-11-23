package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.EventParticipant
import java.util.UUID

@Repository
interface EventParticipantRepository : JpaRepository<EventParticipant, UUID> {
    fun existsByUserIdAndEventId(userId: UUID, eventId: UUID): Boolean
    fun countByEventId(eventId: UUID): Int
    fun deleteByUserIdAndEventId(userId: UUID, eventId: UUID)
}