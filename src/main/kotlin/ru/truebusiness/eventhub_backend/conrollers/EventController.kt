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
import ru.truebusiness.eventhub_backend.conrollers.dto.EventDto
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
    fun create(@RequestBody createEventRequestDto: CreateEventRequestDto): ResponseEntity<EventDto> {
        val response = eventService.create(eventMapper.eventDtoToEventModel(createEventRequestDto))
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{eventID}")
    fun update(
        @PathVariable("eventID") eventID: UUID,
        @RequestBody updateEventRequestDto: UpdateEventRequestDto
    ): ResponseEntity<EventDto> {
        val response = eventService.updateByID(eventID, eventMapper.eventDtoToEventModel(updateEventRequestDto))
        return ResponseEntity.ok(response)
    }
}