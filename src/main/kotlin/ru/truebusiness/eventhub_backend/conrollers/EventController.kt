package ru.truebusiness.eventhub_backend.conrollers

import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.EventMapper
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateEventRequestDto
import ru.truebusiness.eventhub_backend.service.EventService

@RestController("/api/v1/events")
class EventController(
    private val eventService: EventService,
    private val eventMapper: EventMapper
) {

    fun createEvent(createEventRequestDto: CreateEventRequestDto) {
        eventService.createEvent(eventMapper.eventDtoToEventModel(createEventRequestDto))
    }
}