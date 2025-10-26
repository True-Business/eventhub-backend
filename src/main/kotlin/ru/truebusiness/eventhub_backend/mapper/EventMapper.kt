package ru.truebusiness.eventhub_backend.mapper

import java.util.UUID
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy
import ru.truebusiness.eventhub_backend.conrollers.dto.events.CreateEventRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.events.EventDto
import ru.truebusiness.eventhub_backend.conrollers.dto.events.UpdateEventRequestDto
import ru.truebusiness.eventhub_backend.repository.entity.Event
import ru.truebusiness.eventhub_backend.service.model.EventModel

@Mapper(componentModel = "spring")
interface EventMapper {

    fun eventDtoToEventModel(eventRequestDto: CreateEventRequestDto): EventModel

    fun eventModelToEventEntity(eventModel: EventModel): Event

    fun eventDtoToEventModel(eventID: UUID, eventRequestDto: UpdateEventRequestDto): EventModel

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun eventModelToEventEntity(eventModel: EventModel, @MappingTarget event: Event)

    fun eventModelToEventDTO(eventModel: EventModel): EventDto

    fun eventToEventModel(event: Event): EventModel
    fun eventsToEventModels(events: List<Event>): List<EventModel>
    fun eventModelsToEventDTOs(response: List<EventModel>): List<EventDto>
}