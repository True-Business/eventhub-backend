package ru.truebusiness.eventhub_backend.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.Event

@Repository
interface EventRepository : JpaRepository<Event, UUID>