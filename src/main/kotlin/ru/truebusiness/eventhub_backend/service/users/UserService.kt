package ru.truebusiness.eventhub_backend.service.users

import jakarta.transaction.Transactional
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.exceptions.users.OrganizationCreatorException
import ru.truebusiness.eventhub_backend.exceptions.users.UserAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.users.UserNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.EventMapper
import ru.truebusiness.eventhub_backend.mapper.UserMapper
import ru.truebusiness.eventhub_backend.repository.EventRepository
import ru.truebusiness.eventhub_backend.repository.OrganizationRepository
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.UserSpecs
import ru.truebusiness.eventhub_backend.repository.entity.Event
import ru.truebusiness.eventhub_backend.repository.entity.EventStatus
import ru.truebusiness.eventhub_backend.repository.entity.Organization
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.service.EmailService
import ru.truebusiness.eventhub_backend.service.model.EventModel
import ru.truebusiness.eventhub_backend.service.model.UpdateUserModel
import ru.truebusiness.eventhub_backend.service.model.UserFiltersModel
import ru.truebusiness.eventhub_backend.service.model.UserModel
import java.util.UUID

@Service
class UserService(
    private val emailService: EmailService,
    private val eventRepository: EventRepository,
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val eventMapper: EventMapper
) {
    private val log by logger()

    @Transactional
    fun update(updateUserModel: UpdateUserModel): UserModel {
        log.info("Updating user: ${updateUserModel.id}")

        val user: User = userRepository.findById(updateUserModel.id)
            .orElseThrow { UserNotFoundException.withId(updateUserModel.id) }

        val userWithShortId: User? = userRepository.findUserByShortId(updateUserModel.shortId)
        if (userWithShortId != null && userWithShortId.id != user.id)
            throw UserAlreadyExistsException.withShortId(updateUserModel.shortId)

        userMapper.updateUserModelToUserEntity(updateUserModel, user)

        val updatedUser = userRepository.save(user)
        log.info("User updated successfully!")
        return userMapper.userEntityToUserModel(updatedUser)
    }

    fun getByID(id: UUID): UserModel {
        val user = userRepository.findById(id).orElseThrow {
            UserNotFoundException.withId(id)
        }

        log.info("User {} found", id)
        return userMapper.userEntityToUserModel(
            user
        )
    }

    fun findUsers(filter: UserFiltersModel): List<UserModel> {
        val spec = UserSpecs.withUsername(filter.username)
            .and(UserSpecs.isFriendOf(filter.userIdFriend))
            .and(UserSpecs.hasFriendRequestTo(filter.userIdRequestTo))
            .and(UserSpecs.hasFriendRequestFrom(filter.userIdRequestFrom))
            .and(UserSpecs.isParticipantOf(filter.eventIdParticipant))
            .and(UserSpecs.isAdminOf(filter.organizationIdAdmin))

        val filteredUsers = userRepository.findAll(spec)
        log.debug("Found ${filteredUsers.size} users")
        return userMapper.userEntitiesToUserModels(filteredUsers)
    }

    @Transactional
    fun deleteById() {
        val userId = SecurityContextHolder.getContext().authentication.principal as UUID

        val organizations = organizationRepository.findAllByCreatorId(userId)
        if (organizations.isNotEmpty()) {
            throw OrganizationCreatorException.withId(userId)
        }

        val user = userRepository.findById(userId).orElseThrow()
        val organizationIds = user.administratedOrganizations.stream()
            .map(Organization::id)
            .toList()

        val canceledEvents: MutableList<Event> = mutableListOf()

        val events = eventRepository.findPersonalEvents(userId, EventStatus.PLANNED)
        events.stream()
            .forEach { event ->
                if (!organizationIds.contains(event.organizationId)) {
                    event.status = EventStatus.CANCELED
                    canceledEvents.add(event)
                }
            }

        eventRepository.saveAll(canceledEvents)

        userRepository.deleteById(userId)

        canceledEvents.forEach { event ->
            event.participants.forEach { user ->
                val eventModel: EventModel = eventMapper.eventToEventModel(event)
                val email = user.credentials?.email
                if (email != null) {
                    emailService.sendEventCancellation(email, eventModel)
                }
            }
        }
    }

    fun getEvents(userId: UUID): List<EventModel> {
        val events = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException.withId(userId) }.events
        return eventMapper.eventsToEventModels(events)
    }
}