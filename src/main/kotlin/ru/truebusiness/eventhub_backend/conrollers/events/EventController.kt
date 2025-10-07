package ru.truebusiness.eventhub_backend.conrollers.events

import io.swagger.v3.oas.annotations.parameters.RequestBody
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.events.dto.CreateEventRequestDto
import ru.truebusiness.eventhub_backend.conrollers.events.dto.EventDto
import ru.truebusiness.eventhub_backend.conrollers.events.dto.UpdateEventRequestDto

@RestController
@RequestMapping("/event")
interface EventController {
    @PostMapping
    fun create(
        @RequestBody
        createEventRequestDto: CreateEventRequestDto
    ): ResponseEntity<EventDto>

    @PutMapping("/{eventID}")
    fun updateById(
        @PathVariable
        eventID: UUID,
        @RequestBody
        updateEventRequestDto: UpdateEventRequestDto
    ): ResponseEntity<EventDto>
}