package ru.truebusiness.eventhub_backend.exceptions.organization

import java.util.UUID
import org.springframework.security.access.AccessDeniedException

class WrongOrganizerException private constructor(
    message: String,
) : AccessDeniedException(message) {
    companion object {
        fun organizerIDDoesNotMatchUserID(
            eventId: UUID, userId: UUID
        ): WrongOrganizerException {
            return WrongOrganizerException("User with id $userId is not organizer of event with id ${eventId}!")
        }
    }
}