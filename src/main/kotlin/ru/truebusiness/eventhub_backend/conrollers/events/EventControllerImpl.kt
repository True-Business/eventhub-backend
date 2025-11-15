package ru.truebusiness.eventhub_backend.conrollers.events

import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.EventSearchFilter
import ru.truebusiness.eventhub_backend.conrollers.dto.events.CreateEventRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.events.EventDto
import ru.truebusiness.eventhub_backend.conrollers.dto.events.UpdateEventRequestDto
import ru.truebusiness.eventhub_backend.mapper.EventMapper
import ru.truebusiness.eventhub_backend.service.EventService

@RestController
class EventControllerImpl(
    private val eventService: EventService, private val eventMapper: EventMapper
) : EventController {
    override fun create(
        createEventRequestDto: CreateEventRequestDto,
    ): ResponseEntity<EventDto> {
        val model = eventService.create(
            eventMapper.eventDtoToCreateEventModel(createEventRequestDto)
        )
        val dto = eventMapper.eventModelToEventDTO(model)
        return ResponseEntity(dto, HttpStatus.CREATED)
    }

    override fun updateById(
        eventID: UUID,
        updateEventRequestDto: UpdateEventRequestDto,
    ): ResponseEntity<EventDto> {
        val response = eventService.update(
            eventMapper.eventDtoToEventModel(
                eventID, updateEventRequestDto
            )
        )
        return ResponseEntity.ok(eventMapper.eventModelToEventDTO(response))
    }

    override fun getById(eventID: UUID): ResponseEntity<EventDto> {
        val response = eventService.get(eventID)
        return ResponseEntity.ok(eventMapper.eventModelToEventDTO(response))
    }

    override fun deleteDraft(eventID: UUID) {
        eventService.deleteDraft(eventID)
    }

    override fun search(eventSearchFilter: EventSearchFilter): ResponseEntity<List<EventDto>> {
        val response = eventService.search(eventSearchFilter)
        return ResponseEntity.ok(eventMapper.eventModelsToEventDTOs(response))
    }
}