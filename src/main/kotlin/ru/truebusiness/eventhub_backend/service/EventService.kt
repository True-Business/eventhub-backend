package ru.truebusiness.eventhub_backend.service

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateEventRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.NewEventResponse
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.EventMapper
import ru.truebusiness.eventhub_backend.repository.EventRepository
import ru.truebusiness.eventhub_backend.repository.entity.Event
import ru.truebusiness.eventhub_backend.service.model.EventModel

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
) {
    private val log by logger()

    @Operation(
        summary = "Создание нового мероприятия",
        description = """
        Создает новое мероприятие с указанными параметрами.
        Обязательно указать название мероприятия (поле name).
        
        Для приватных мероприятий (isOpen = false) рекомендуется указать:
        - organizerId
        - organizationId
    """,
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания мероприятия",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = CreateEventRequestDto::class),
                examples = [
                    ExampleObject(
                        name = "Публичное бесплатное мероприятие",
                        summary = "Конференция с регистрацией и ограничением по количеству участников",
                        value = """{
                        "name": "Конференция разработчиков 2023",
                        "startDate": "2023-11-15",
                        "startTime": "09:00:00",
                        "endTime": "18:00:00",
                        "address": "Москва, Красная площадь, 1",
                        "description": "Ежегодная конференция для разработчиков",
                        "isFree": true,
                        "isOpen": true,
                        "eventStatus": "PUBLISHED",
                        "city": "Москва",
                        "isWithRegister": true,
                        "peopleLimit": 500,
                        "registerEndDate": "2023-11-10",
                        "registerEndTime": "23:59:59"
                    }"""
                    ),
                    ExampleObject(
                        name = "Приватное платное мероприятие",
                        summary = "Закрытое мероприятие для сотрудников организации",
                        value = """{
                        "name": "Внутренний хакатон",
                        "startDate": "2023-12-01",
                        "startTime": "10:00:00",
                        "endTime": "17:00:00",
                        "address": "Санкт-Петербург, Невский проспект, 10",
                        "description": "Внутренний хакатон для сотрудников компании",
                        "isFree": false,
                        "price": 999.99,
                        "isOpen": false,
                        "organizerId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                        "organizationId": "b1ffcd00-1d1c-5fa9-cc7e-7cc0ce491b22",
                        "eventStatus": "DRAFT",
                        "city": "Санкт-Петербург"
                    }"""
                    )
                ]
            )]
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Мероприятие успешно создано",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = NewEventResponse::class),
                    examples = [
                        ExampleObject(
                            name = "Успешное создание",
                            summary = "Возвращается идентификатор и название мероприятия",
                            value = """{
                            "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                            "eventName": "Конференция разработчиков 2023"
                        }"""
                        )
                    ]
                )]
            ),
        ]
    )
    @Transactional
    fun createEvent(eventModel: EventModel): NewEventResponse {
        log.info("Creating new event: ${eventModel.name}")

        val event: Event = eventMapper.eventModelToEventEntity(eventModel)
        val newEvent = eventRepository.save(event)

        log.info("New event created successfully!")
        return NewEventResponse(
            newEvent.id,
            newEvent.name
        )
    }
}