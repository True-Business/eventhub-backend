package ru.truebusiness.eventhub_backend.exceptions.events

import java.util.UUID

class EventNotDraftException private constructor(
    message: String,
) : EventsException(message) {
    companion object {
        fun byId(id: UUID) =
            EventNotDraftException("Event with id $id is not draft!")
    }
}