package ru.truebusiness.eventhub_backend.service

import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.repository.EventRepository
import ru.truebusiness.eventhub_backend.service.model.EventModel

@Service
class EventService(
    private val eventRepository: EventRepository,
) {
    fun createEvent(event: EventModel) {

    }
}