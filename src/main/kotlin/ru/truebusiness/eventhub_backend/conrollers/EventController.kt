package ru.truebusiness.eventhub_backend.conrollers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.mapper.EventMapper
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateEventRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.NewEventResponse
import ru.truebusiness.eventhub_backend.conrollers.dto.UpdateEventRequestDto
import ru.truebusiness.eventhub_backend.service.EventService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/event")
class EventController(
    private val eventService: EventService,
    private val eventMapper: EventMapper
) {
    @PostMapping
    fun createEvent(@RequestBody createEventRequestDto: CreateEventRequestDto): ResponseEntity<NewEventResponse> {
        val response = eventService.createEvent(eventMapper.eventDtoToEventModel(createEventRequestDto))
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{eventID}")
    fun updateEvent(
        @PathVariable("eventID") eventID: UUID,
        @RequestBody updateEventRequestDto: UpdateEventRequestDto
    ): ResponseEntity<NewEventResponse> {
        val response = eventService.updateEvent(eventID, eventMapper.eventDtoToEventModel(updateEventRequestDto))
        return ResponseEntity.ok(response)
    }
}