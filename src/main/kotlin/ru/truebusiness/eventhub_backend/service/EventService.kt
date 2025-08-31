package ru.truebusiness.eventhub_backend.service

import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.EventMapper
import ru.truebusiness.eventhub_backend.repository.EventRepository
import ru.truebusiness.eventhub_backend.repository.entity.Event
import ru.truebusiness.eventhub_backend.service.model.EventModel

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
) {
    fun createEvent(eventModel: EventModel) {
        val event: Event = eventMapper.eventModelToEventEntity(eventModel)
        eventRepository.save(event)
    }
}