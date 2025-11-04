package ru.truebusiness.eventhub_backend.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.Event
import java.time.Instant

@Repository
interface EventRepository : JpaRepository<Event, UUID> {

    @Query(value = """
    SELECT * FROM events e
    WHERE 
        (:city IS NULL OR e.city = :city)
        AND (:minPrice IS NULL OR e.price >= :minPrice)
        AND (:maxPrice IS NULL OR e.price <= :maxPrice)
        AND (CAST(:startDateTime AS timestamptz) IS NULL OR e.start_datetime >= :startDateTime)
        AND (:organizerId IS NULL OR e.organizer_id = :organizerId)
        AND (:isOpen IS NULL OR e.is_open = :isOpen)
        AND (:status IS NULL OR e.status = :status)
        AND (
            :minDurationMinutes IS NULL 
            OR (e.end_datetime IS NOT NULL AND 
                EXTRACT(EPOCH FROM (e.end_datetime - e.start_datetime)) / 60 >= :minDurationMinutes)
        )
        AND (
            :maxDurationMinutes IS NULL 
            OR (e.end_datetime IS NOT NULL AND 
                EXTRACT(EPOCH FROM (e.end_datetime - e.start_datetime)) / 60 <= :maxDurationMinutes)
        )
    ORDER BY e.start_datetime
    """, nativeQuery = true)
    fun findByFilter(
        @Param("city") city: String?,
        @Param("minPrice") minPrice: Double?,
        @Param("maxPrice") maxPrice: Double?,
        @Param("startDateTime") startDateTime: Instant?,
        @Param("minDurationMinutes") minDurationMinutes: Int?,
        @Param("maxDurationMinutes") maxDurationMinutes: Int?,
        @Param("organizerId") organizerId: UUID?,
        @Param("isOpen") isOpen: Boolean?,
        @Param("status") status: String?,
    ): List<Event>
}