package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import java.util.UUID
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.exceptions.events.EventNotDraftException
import ru.truebusiness.eventhub_backend.exceptions.events.EventNotFoundException
import ru.truebusiness.eventhub_backend.exceptions.organization.WrongOrganizerException
import ru.truebusiness.eventhub_backend.conrollers.dto.EventSearchFilter
import ru.truebusiness.eventhub_backend.exceptions.NotImplementedException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.EventMapper
import ru.truebusiness.eventhub_backend.repository.EventRepository
import ru.truebusiness.eventhub_backend.repository.entity.Event
import ru.truebusiness.eventhub_backend.repository.entity.EventStatus
import ru.truebusiness.eventhub_backend.service.model.CreateEventModel
import ru.truebusiness.eventhub_backend.service.model.EventModel

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
) {
    private val log by logger()

    @Transactional
    fun create(eventModel: CreateEventModel): EventModel {
        log.info("Creating new event: {}", eventModel.name)

        val event: Event = eventMapper.eventModelToEventEntity(eventModel)
        val newEvent = eventRepository.save(event)

        log.info("New event created {}", newEvent.id)
        return eventMapper.eventToEventModel(newEvent)
    }

    @Transactional
    fun update(eventModel: EventModel): EventModel {
        log.info("Updating event: {}", eventModel.id)

        val event: Event = eventRepository.findById(eventModel.id).orElseThrow {
            EventNotFoundException.byId(eventModel.id)
        }

        val userID = SecurityContextHolder.getContext().authentication.principal as UUID
        if (event.organizerId != userID) {
            throw WrongOrganizerException.organizerIDDoesNotMatchUserID(
                event.id, userID
            )
        }

        eventMapper.eventModelToEventEntity(eventModel, event)
        val updatedEvent = eventRepository.save(event)

        log.info("Updated event: {}", eventModel.id)
        return eventMapper.eventToEventModel(updatedEvent)
    }

    fun get(eventID: UUID): EventModel {
        log.info("Get event: {}", eventID)

        val event: Event = eventRepository.findById(eventID).orElseThrow {
            EventNotFoundException.byId(eventID)
        }

        return eventMapper.eventToEventModel(event)
    }

    fun deleteDraft(eventID: UUID) {
        log.info("Deleting draft event: {}", eventID)

        val userID = SecurityContextHolder.getContext().authentication.principal as UUID
        val event = eventRepository.findById(eventID)
            .orElseThrow{EventNotFoundException.byId(eventID)}

        if (event.organizerId != userID) {
            throw WrongOrganizerException.organizerIDDoesNotMatchUserID(
                eventID, userID
            )
        }
        if (event.status != EventStatus.DRAFT) {
            throw EventNotDraftException.byId(eventID)
        }

        eventRepository.deleteById(eventID)

        log.info("Event {} deleted successfully!", eventID)
    }

    fun search(eventSearchFilter: EventSearchFilter): List<EventModel> {
        log.info("Search events")

        if (eventSearchFilter.isParticipant != null) {
            throw NotImplementedException("isParticipant not implemented", null)
        }

        val events = eventRepository.findByFilter(
            eventSearchFilter.city, eventSearchFilter.minPrice, eventSearchFilter.maxPrice,
            eventSearchFilter.startDateTime, eventSearchFilter.minDurationMinutes, eventSearchFilter.maxDurationMinutes,
            eventSearchFilter.organizerId, eventSearchFilter.isOpen, eventSearchFilter.status?.toString()
        )

        return eventMapper.eventsToEventModels(events)
    }
}