package ru.truebusiness.eventhub_backend.mapper

import java.util.UUID
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy
import ru.truebusiness.eventhub_backend.conrollers.dto.events.*
import ru.truebusiness.eventhub_backend.repository.entity.Event
import ru.truebusiness.eventhub_backend.repository.entity.EventParticipant
import ru.truebusiness.eventhub_backend.service.model.CreateEventModel
import ru.truebusiness.eventhub_backend.service.model.EventModel
import ru.truebusiness.eventhub_backend.service.model.EventParticipantModel

@Mapper(componentModel = "spring")
interface EventMapper {

    @Mapping(source = "open", target = "isOpen")
    @Mapping(source = "withRegister", target = "isWithRegister")
    fun eventDtoToEventModel(eventRequestDto: CreateEventRequestDto): EventModel

    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    @Mapping(source = "open", target = "isOpen")
    @Mapping(source = "withRegister", target = "isWithRegister")
    fun eventDtoToCreateEventModel(createEventRequestDto: CreateEventRequestDto): CreateEventModel

    @Mapping(source = "open", target = "isOpen")
    @Mapping(source = "withRegister", target = "isWithRegister")
    fun eventModelToEventEntity(eventModel: EventModel): Event

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(source = "open", target = "isOpen")
    @Mapping(source = "withRegister", target = "isWithRegister")
    fun eventModelToEventEntity(createEventModel: CreateEventModel): Event

    @Mapping(source = "eventRequestDto.eventCategory", target = "category")
    @Mapping(source = "eventRequestDto.eventStatus", target = "status")
    @Mapping(source = "eventRequestDto.open", target = "isOpen")
    @Mapping(source = "eventRequestDto.withRegister", target = "isWithRegister")
    fun eventDtoToEventModel(eventID: UUID, eventRequestDto: UpdateEventRequestDto): EventModel

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "open", target = "open")
    @Mapping(source = "withRegister", target = "withRegister")
    fun eventModelToEventEntity(eventModel: EventModel, @MappingTarget event: Event)

    @Mapping(source = "open", target = "isOpen")
    @Mapping(source = "withRegister", target = "isWithRegister")
    @Mapping(source = "userParticipant", target = "isUserParticipant")
    fun eventModelToEventDTO(eventModel: EventModel): EventDto

    fun eventToEventModel(event: Event): EventModel
    fun eventsToEventModels(events: List<Event>): List<EventModel>
    fun eventModelsToEventDTOs(response: List<EventModel>): List<EventDto>

    fun eventParticipantToEventParticipantModel(
        eventParticipant: EventParticipant): EventParticipantModel
    fun eventParticipantModelToEventParticipantDto(
        eventParticipantModel: EventParticipantModel): EventParticipantDto
}
