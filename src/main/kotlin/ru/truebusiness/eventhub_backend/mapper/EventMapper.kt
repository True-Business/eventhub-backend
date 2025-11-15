package ru.truebusiness.eventhub_backend.mapper

import java.util.UUID
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy
import ru.truebusiness.eventhub_backend.conrollers.dto.events.CreateEventRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.events.EventDto
import ru.truebusiness.eventhub_backend.conrollers.dto.events.UpdateEventRequestDto
import ru.truebusiness.eventhub_backend.repository.entity.Event
import ru.truebusiness.eventhub_backend.service.model.CreateEventModel
import ru.truebusiness.eventhub_backend.service.model.EventModel

@Mapper(componentModel = "spring")
interface EventMapper {

    fun eventDtoToEventModel(eventRequestDto: CreateEventRequestDto): EventModel

    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    fun eventDtoToCreateEventModel(createEventRequestDto: CreateEventRequestDto): CreateEventModel

    fun eventModelToEventEntity(eventModel: EventModel): Event

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    fun eventModelToEventEntity(createEventModel: CreateEventModel): Event

    fun eventDtoToEventModel(eventID: UUID, eventRequestDto: UpdateEventRequestDto): EventModel

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun eventModelToEventEntity(eventModel: EventModel, @MappingTarget event: Event)

    @Mapping(source = "open", target = "isOpen")
    @Mapping(source = "withRegister", target = "isWithRegister")
    fun eventModelToEventDTO(eventModel: EventModel): EventDto

    fun eventToEventModel(event: Event): EventModel
    fun eventsToEventModels(events: List<Event>): List<EventModel>
    fun eventModelsToEventDTOs(response: List<EventModel>): List<EventDto>
}