package ru.truebusiness.eventhub_backend.exceptions.events

open class EventsException(
    override val message: String
) : RuntimeException(message)