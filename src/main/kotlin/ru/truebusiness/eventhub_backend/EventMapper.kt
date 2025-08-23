package ru.truebusiness.eventhub_backend

import org.mapstruct.Mapper
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateEventRequestDto
import ru.truebusiness.eventhub_backend.service.model.EventModel

@Mapper(componentModel = "spring")
interface EventMapper {
    fun eventDtoToEventModel(eventRequestDto: CreateEventRequestDto): EventModel
}