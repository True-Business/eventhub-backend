package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.exceptions.events.EventNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.EventMapper
import ru.truebusiness.eventhub_backend.repository.EventRepository
import ru.truebusiness.eventhub_backend.repository.entity.Event
import ru.truebusiness.eventhub_backend.service.model.EventModel
import java.util.UUID

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
) {
    private val log by logger()

    @Transactional
    fun create(eventModel: EventModel): EventModel {
        log.info("Creating new event: ${eventModel.name}")

        val event: Event = eventMapper.eventModelToEventEntity(eventModel)
        val newEvent = eventRepository.save(event)

        log.info("New event created successfully!")
        return eventMapper.eventToEventModel(newEvent)
    }

    @Transactional
    fun update(eventModel: EventModel): EventModel {
        log.info("Updating event: ${eventModel.id}")

        val event: Event = eventRepository.findById(eventModel.id).orElseThrow {
            EventNotFoundException.byId(eventModel.id)
        }
        eventMapper.eventModelToEventEntity(eventModel, event)

        val updatedEvent = eventRepository.save(event)

        log.info("Event updated successfully!")
        return eventMapper.eventToEventModel(updatedEvent)
    }

    fun get(eventID: UUID): EventModel {
        log.info("Get event: {}", eventID)

        val event: Event = eventRepository.findById(eventID).orElseThrow {
            EventNotFoundException.byId(eventID)
        }

        return eventMapper.eventToEventModel(event)
    }
}