package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.Event
import java.util.UUID

@Repository
interface EventRepository : JpaRepository<Event, UUID>