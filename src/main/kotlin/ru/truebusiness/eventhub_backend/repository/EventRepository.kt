package ru.truebusiness.eventhub_backend.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.conrollers.dto.EventSearchFilter
import ru.truebusiness.eventhub_backend.repository.entity.Event

@Repository
interface EventRepository : JpaRepository<Event, UUID> {

    @Query("""
        SELECT e.* FROM events e
        WHERE 
            (:#{#eventSearchFilter.city} IS NULL OR e.city = :#{#eventSearchFilter.city})
            AND (:#{#eventSearchFilter.minPrice} IS NULL OR e.price >= :#{#eventSearchFilter.minPrice})
            AND (:#{#eventSearchFilter.maxPrice} IS NULL OR e.price <= :#{#eventSearchFilter.maxPrice})
            AND (:#{#eventSearchFilter.startDateTime} IS NULL OR e.start_datetime >= :#{#eventSearchFilter.startDateTime})
            AND (:#{#eventSearchFilter.organizerId} IS NULL OR e.organizer_id = :#{#eventSearchFilter.organizerId})
            AND (:#{#eventSearchFilter.isOpen} IS NULL OR e.is_open = :#{#eventSearchFilter.isOpen})
            AND (:#{#eventSearchFilter.status} IS NULL OR e.status = :#{#eventSearchFilter.status?.name()})
            AND (
                :#{#eventSearchFilter.minDurationMinutes} IS NULL 
                OR (e.end_datetime IS NOT NULL AND EXTRACT(EPOCH FROM (e.end_datetime - e.start_datetime)) / 60 >= :#{#eventSearchFilter.minDurationMinutes})
            )
            AND (
                :#{#eventSearchFilter.maxDurationMinutes} IS NULL 
                OR (e.end_datetime IS NOT NULL AND EXTRACT(EPOCH FROM (e.end_datetime - e.start_datetime)) / 60 <= :#{#eventSearchFilter.maxDurationMinutes})
            )
        ORDER BY e.start_datetime
    """, nativeQuery = true)
    fun findByFilter(eventSearchFilter: EventSearchFilter): List<Event>
}