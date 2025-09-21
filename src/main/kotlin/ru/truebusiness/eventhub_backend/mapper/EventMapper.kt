package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateEventRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.UpdateEventRequestDto
import ru.truebusiness.eventhub_backend.repository.entity.Event
import ru.truebusiness.eventhub_backend.service.model.EventModel
import java.util.UUID

@Mapper(componentModel = "spring")
interface EventMapper {

    fun eventDtoToEventModel(eventRequestDto: CreateEventRequestDto): EventModel

    fun eventModelToEventEntity(eventModel: EventModel): Event

    fun eventDtoToEventModel(eventRequestDto: UpdateEventRequestDto): EventModel

    @Mapping(source = "eventID", target = "id")
    fun eventModelToEventEntity(eventID: UUID, eventModel: EventModel): Event
}