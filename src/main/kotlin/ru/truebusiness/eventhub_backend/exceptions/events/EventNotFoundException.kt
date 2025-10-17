package ru.truebusiness.eventhub_backend.exceptions.events

import java.util.UUID

class EventNotFoundException private constructor(
    message: String
) : EventsException(message) {
    companion object {
        fun byId(id: UUID) = EventNotFoundException(
            "Event with id $id doesn't exist!"
        )
    }
}