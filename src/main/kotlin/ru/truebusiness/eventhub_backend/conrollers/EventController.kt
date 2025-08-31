package ru.truebusiness.eventhub_backend.conrollers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.EventMapper
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateEventRequestDto
import ru.truebusiness.eventhub_backend.service.EventService

@RestController
@RequestMapping("/api/v1/event")
class EventController(
    private val eventService: EventService,
    private val eventMapper: EventMapper
) {
    @PostMapping
    fun createEvent(@RequestBody createEventRequestDto: CreateEventRequestDto) {
        eventService.createEvent(eventMapper.eventDtoToEventModel(createEventRequestDto))
    }
}