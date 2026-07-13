package ru.truebusiness.eventhub_backend.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.transaction.Transactional
import java.util.UUID
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.exceptions.events.EventNotDraftException
import ru.truebusiness.eventhub_backend.exceptions.events.EventNotFoundException
import ru.truebusiness.eventhub_backend.exceptions.organization.WrongOrganizerException
import ru.truebusiness.eventhub_backend.conrollers.dto.EventSearchFilter
import ru.truebusiness.eventhub_backend.exceptions.NotImplementedException
import ru.truebusiness.eventhub_backend.exceptions.events.RegistrationException
import ru.truebusiness.eventhub_backend.exceptions.users.UserNotFoundException
import ru.truebusiness.eventhub_backend.mapper.EventMapper
import ru.truebusiness.eventhub_backend.mapper.UserMapper
import ru.truebusiness.eventhub_backend.repository.EventParticipantRepository
import ru.truebusiness.eventhub_backend.repository.EventRepository
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.entity.Event
import ru.truebusiness.eventhub_backend.repository.entity.EventParticipant
import ru.truebusiness.eventhub_backend.repository.entity.EventStatus
import ru.truebusiness.eventhub_backend.service.model.*
import java.time.Instant

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventParticipantRepository: EventParticipantRepository,
    private val userRepository: UserRepository,
    private val eventMapper: EventMapper,
    private val userMapper: UserMapper,
) {
    companion object {
        private val log = KotlinLogging.logger {}
    }

    @Transactional
    fun create(eventModel: CreateEventModel): EventModel {
        log.info { "${"Creating new event: {}"} $eventModel" }

        val event: Event = eventMapper.eventModelToEventEntity(eventModel)
        val newEvent = eventRepository.save(event)
        val organizerId = newEvent.organizerId

        if (!userRepository.existsById(organizerId)) {
            throw UserNotFoundException.withId(organizerId)
        }

        if (!eventParticipantRepository.existsByUserIdAndEventId(organizerId, newEvent.id)) {
            eventParticipantRepository.save(EventParticipant(userId = organizerId, eventId = newEvent.id))
        }

        val createdEventModel = eventMapper.eventToEventModel(newEvent)
        createdEventModel.isUserParticipant = true
        createdEventModel.isOwner = true
        createdEventModel.participantsCount = 1

        log.info { "${"New event created {}"} ${newEvent.id}" }
        return createdEventModel
    }

    @Transactional
    fun update(eventModel: EventModel): EventModel {
        log.info { "${"Updating event: {}"} ${eventModel.id}" }

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

        log.info { "${"Updated event: {}"} ${eventModel.id}" }
        return eventMapper.eventToEventModel(updatedEvent)
    }

    fun get(eventID: UUID): EventModel {
        log.info { "${"Get event: {}"} $eventID" }

        val event: Event = eventRepository.findById(eventID).orElseThrow {
            EventNotFoundException.byId(eventID)
        }

        val userId = SecurityContextHolder.getContext().authentication.principal as UUID
        val eventModel = eventMapper.eventToEventModel(event)
        eventModel.isUserParticipant = eventParticipantRepository.existsByUserIdAndEventId(userId, eventID)
        eventModel.isOwner = event.organizerId == userId
        eventModel.participantsCount = eventParticipantRepository.countByEventId(eventID)

        return eventModel
    }

    fun deleteDraft(eventID: UUID) {
        log.info { "${"Deleting draft event: {}"} $eventID" }

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

        log.info { "${"Event {} deleted successfully!"} $eventID" }
    }

    @Transactional
    fun delete(eventID: UUID) {
        log.info { "Deleting event: $eventID" }

        val userID = SecurityContextHolder.getContext().authentication.principal as UUID
        val event = eventRepository.findById(eventID)
            .orElseThrow { EventNotFoundException.byId(eventID) }

        if (event.organizerId != userID) {
            throw WrongOrganizerException.organizerIDDoesNotMatchUserID(
                eventID, userID
            )
        }

        eventRepository.deleteById(eventID)
        log.info { "Event $eventID deleted successfully!" }
    }

    fun search(eventSearchFilter: EventSearchFilter): List<EventModel> {
        log.info { "Search events" }
        log.info { "${"isopen: {}"} ${eventSearchFilter.isOpen}" }

        val userId = SecurityContextHolder.getContext().authentication.principal as UUID
        if (eventSearchFilter.isParticipant != null) {
            throw NotImplementedException("isParticipant not implemented", null)
        }

        val events = eventRepository.findByFilter(
            eventSearchFilter.city, eventSearchFilter.minPrice, eventSearchFilter.maxPrice,
            eventSearchFilter.startDateTime, eventSearchFilter.minDurationMinutes, eventSearchFilter.maxDurationMinutes,
            eventSearchFilter.organizerId, eventSearchFilter.isOpen, eventSearchFilter.status?.toString()
        )

        val eventModels = mutableListOf<EventModel>()
        for (event in events) {
            // TODO: возможно выгоднее сделать поиск всех людей и потом рабоать с этим списком, чтобы не делать 2
            //  запроса к БД. Хотя если участников будет много, то наверное текущее решение выгоднее
            val eventModel = eventMapper.eventToEventModel(event)
            eventModel.isUserParticipant = eventParticipantRepository.existsByUserIdAndEventId(userId, event.id)
            eventModel.isOwner = event.organizerId == userId
            eventModel.participantsCount = eventParticipantRepository.countByEventId(event.id)
            eventModels.add(eventModel)
        }

        return eventModels
    }

    @Transactional
    fun registerToEvent(eventId: UUID, userId: UUID): EventParticipantModel {
        val event = eventRepository.findById(eventId).orElseThrow {
            EventNotFoundException.byId(eventId)
        }
        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException.withId(userId)
        }
        if (eventParticipantRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw RegistrationException.alreadyRegistered(userId, eventId)
        }

        if (event.status != EventStatus.PLANNED) {
            throw RegistrationException.eventIsUnavailable(eventId)
        }

        event.registerEndDateTime?.let {
            if (it < Instant.now()) {
                throw RegistrationException.registrationEnded(eventId)
            }
        }

        val numParticipants = eventParticipantRepository.countByEventId(eventId)
        event.peopleLimit?.let {
            if (it <= numParticipants) {
                throw RegistrationException.participantsLimitReached(eventId)
            }
        }

        val eventParticipant = eventParticipantRepository.save(
            EventParticipant(userId = userId, eventId = eventId)
        )
        log.info { "User $userId registered to event $eventId" }
        return eventMapper.eventParticipantToEventParticipantModel(eventParticipant)
    }

    @Transactional
    fun unregisterFromEvent(eventId: UUID) {
        val event = get(eventId)
        if (event.status != EventStatusModel.PLANNED) {
            throw RegistrationException.eventIsUnavailable(eventId)
        }

        val userId = SecurityContextHolder.getContext().authentication.principal as UUID
        if (!eventParticipantRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw RegistrationException.isNotRegistered(userId, eventId)
        }

        eventParticipantRepository.deleteByUserIdAndEventId(userId, eventId)
        log.info { "User $userId unsubscribed from event $eventId" }
    }

    fun getEventParticipants(eventId: UUID): List<UserModel> {
        if (!eventRepository.existsById(eventId)) {
            throw EventNotFoundException.byId(eventId)
        }

        val participantIds = eventParticipantRepository.findByEventId(eventId)
            .map { it.userId }
        val participants = userRepository.findAllById(participantIds)
        return userMapper.userEntitiesToUserModels(participants)
    }
}
