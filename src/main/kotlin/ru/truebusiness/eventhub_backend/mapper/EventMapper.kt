package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateEventRequestDto
import ru.truebusiness.eventhub_backend.repository.entity.Event
import ru.truebusiness.eventhub_backend.service.model.EventModel

@Mapper(componentModel = "spring")
interface EventMapper {

    @Mapping(source = "draft", target = "draft", defaultValue = "true")
    fun eventDtoToEventModel(eventRequestDto: CreateEventRequestDto): EventModel

    fun eventModelToEventEntity(eventModel: EventModel): Event
}