package ru.truebusiness.eventhub_backend.exceptions.events

import java.util.UUID

class RegistrationException private constructor(
    message: String
) : EventsException(message) {
    companion object {
        fun eventIsUnavailable(eventId: UUID): RegistrationException {
            return RegistrationException("Event $eventId is not available")
        }

        fun registrationEnded(eventId: UUID): RegistrationException {
            return RegistrationException("Registration to event $eventId ended")
        }

        fun participantsLimitReached(eventId: UUID): RegistrationException {
            return RegistrationException("Reached participants limit on event $eventId")
        }

        fun alreadyRegistered(userId: UUID, eventId: UUID): RegistrationException {
            return RegistrationException("User $userId is already registered to event $eventId")
        }

        fun isNotRegistered(userId: UUID, eventId: UUID): RegistrationException {
            return RegistrationException("User $userId is not registered to event $eventId")
        }
    }
}