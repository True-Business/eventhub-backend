package ru.truebusiness.eventhub_backend.conrollers.events

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.ErrorResponseDto
import ru.truebusiness.eventhub_backend.conrollers.dto.EventSearchFilter
import ru.truebusiness.eventhub_backend.conrollers.dto.events.*

@RestController
@RequestMapping("/api/v1/event")
interface EventController {
    @PostMapping
    @Operation(
        summary = "Создание нового события",
        description = """
            Создает новое событие с указанными параметрами.
            <p><b>ВАЖНО:</b> После создания событие находится в статусе DRAFT до публикации.
            <p>Для публикации используйте метод обновления со статусом PUBLISHED.
            <p><b>Примечание по датам:</b> Все даты должны быть в формате ISO 8601 с временной зоной UTC (Z).
        """,
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Событие успешно создано",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = EventDto::class),
                    examples = [ExampleObject(
                        name = "Пример созданного события",
                        value = """{
                                "id": "550e8400-e29b-41d4-a716-446655440000",
                                "name": "Конференция по Kotlin",
                                "startDateTime": "2025-04-05T10:00:00",
                                "endDateTime": "2025-04-05T17:00:00",
                                "updatedAt": "2023-10-05T12:34:56.789Z",
                                "organizerId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                "organizationId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                                "category": "PLACEHOLDER",
                                "address": "ул. Ленина, д. 15, Москва",
                                "route": "Вход со двора, подъезд 3",
                                "description": "Ежегодная конференция разработчиков на Kotlin",
                                "price": 500.0,
                                "isOpen": true,
                                "status": "DRAFT",
                                "city": "Москва",
                                "isWithRegister": true,
                                "peopleLimit": 100,
                                "registerEndDateTime": "2025-04-01T23:59:59"
                            }""",
                    )]
                )],
            ),
        ]
    )
    fun create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания события",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = CreateEventRequestDto::class),
                examples = [
                    ExampleObject(
                        name = "Полный запрос",
                        value = """{
                            "name": "Конференция по Kotlin",
                            "startDateTime": "2025-04-05T10:00:00",
                            "endDateTime": "2025-04-05T17:00:00",
                            "organizerId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                            "organizationId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                            "category": "PLACEHOLDER",
                            "address": "ул. Ленина, д. 15, Москва",
                            "route": "Вход со двора, подъезд 3",
                            "description": "Ежегодная конференция разработчиков на Kotlin",
                            "price": 500.0,
                            "isOpen": true,
                            "status": "DRAFT",
                            "city": "Москва",
                            "isWithRegister": true,
                            "peopleLimit": 100,
                            "registerEndDateTime": "2025-04-01T23:59:59",
                            "isDraft": true
                        }"""
                    ),
                    ExampleObject(
                        name = "Минимальный запрос",
                        value = """{
                            "name": "Встреча разработчиков",
                            "startDateTime": "2025-04-10T19:00:00",
                            "organizerId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                            "category": "MEETUP",
                            "address": "Кафе 'Уют'",
                            "description": "Неформальная встреча разработчиков",
                            "price": 0.0,
                            "isOpen": true,
                            "status": "DRAFT",
                            "city": "Москва",
                            "isWithRegister": false,
                            "isDraft": true
                        }""",
                        summary = "Без необязательных полей"
                    )
                ]
            )]
        )
        @RequestBody
        createEventRequestDto: CreateEventRequestDto
    ): ResponseEntity<EventDto>

    @PutMapping("/{eventID}")
    @Operation(
        summary = "Обновление информации о событии",
        description = """
            Обновляет данные существующего события. 
            <p><b>ВАЖНО:</b> Поля, не переданные в запросе (как null), остаются без изменений.
            <p><b>Примечание по датам:</b> Все даты должны быть в формате ISO 8601 с временной зоной UTC (Z).
            <p><b>ВАЖНО:</b> Поля organizerId и organizationId в будущих версиях будут автоматически определяться через аутентификационные токены.
        """,
        parameters = [
            Parameter(
                name = "eventID",
                description = "UUID события для обновления",
                required = true,
                example = "550e8400-e29b-41d4-a716-446655440000",
                schema = Schema(
                    type = "string",
                    format = "uuid",
                    example = "550e8400-e29b-41d4-a716-446655440000"
                )
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Событие успешно обновлено",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = EventDto::class),
                    examples = [
                        ExampleObject(
                            name = "Обновленное событие",
                            value = """{
                                "id": "550e8400-e29b-41d4-a716-446655440000",
                                "name": "Конференция по Kotlin 2025",
                                "startDateTime": "2025-04-05T10:00:00Z",
                                "endDateTime": "2025-04-05T18:00:00Z",
                                "updatedAt": "2023-10-06T09:15:00Z",
                                "organizerId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                "organizationId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                                "category": "PLACEHOLDER",
                                "address": "ул. Тверская, д. 10, Москва",
                                "route": "Метро Охотный ряд, выход 3",
                                "description": "Обновленное описание конференции",
                                "price": 750.0,
                                "isOpen": true,
                                "status": "PUBLISHED",
                                "city": "Москва",
                                "isWithRegister": true,
                                "peopleLimit": 150,
                                "registerEndDateTime": "2025-03-25T23:59:59"
                            }"""
                        )
                    ]
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Некорректный формат UUID или данных",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponseDto::class),
                    examples = [
                        ExampleObject(
                            name = "Неверный UUID",
                            value = """{
                                "code": 400,
                                "message": "Invalid UUID string: 'invalid-id'"
                            }"""
                        ),
                        ExampleObject(
                            name = "Некорректный формат даты",
                            value = """{
                                "code": 400,
                                "message": "Invalid date format: '2025/04/05 10:00'"
                            }"""
                        )
                    ]
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Событие не найдено",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponseDto::class),
                    examples = [
                        ExampleObject(
                            name = "Событие не существует",
                            value = """{
                                "code": 404,
                                "message": "Event with id '550e8400-e29b-41d4-a716-446655440000' not found"
                            }"""
                        )
                    ]
                )]
            )
        ]
    )
    fun updateById(
        @PathVariable
        eventID: UUID,
        @RequestBody
        updateEventRequestDto: UpdateEventRequestDto
    ): ResponseEntity<EventDto>

    @GetMapping("/{eventID}")
    fun getById(@PathVariable eventID: UUID): ResponseEntity<EventDto>

    @DeleteMapping("/{eventID}/draft")
    fun deleteDraft(@PathVariable eventID: UUID)

    @PostMapping("/search")
    fun search(@RequestBody eventSearchFilter: EventSearchFilter): ResponseEntity<List<EventDto>>

    @PostMapping("/{eventID}/register")
    @Operation(
        summary = "Регистрация на мероприятие",
        description = "ID пользователя определяется через аутентификационный токен.",
        parameters = [
            Parameter(
                name = "eventID",
                description = "UUID мероприятия для регистрации",
                required = true,
                example = "550e8400-e29b-41d4-a716-446655440000",
                schema = Schema(
                    type = "string",
                    format = "uuid",
                    example = "550e8400-e29b-41d4-a716-446655440000"
                )
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь успешно зарегистрирован",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = EventParticipantDto::class),
                    examples = [
                        ExampleObject(
                            name = "Данные регистрации",
                            value = """{
                                "id": "ca7ef39b-01da-1982-1j90-0a3190bba54c",
                                "userId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                "eventId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                                "registeredAt": "2025-04-01T23:59:59Z"
                            }"""
                        )
                    ]
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Регистрация недоступна или пользователь уже зарегистрирван",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponseDto::class),
                    examples = [
                        ExampleObject(
                            name = "Мероприятие недоступно",
                            value = """{
                                "code": 400,
                                "message": "Event '550e8400-e29b-41d4-a716-446655440000' is not available"
                            }"""
                        ),
                        ExampleObject(
                            name = "Регистрация на мероприятие завершилась",
                            value = """{
                                "code": 400,
                                "message": "Registration to event '550e8400-e29b-41d4-a716-446655440000' ended"
                            }"""
                        ),
                        ExampleObject(
                            name = "Нет мест для регистрации",
                            value = """{
                                "code": 400,
                                "message": "Reached participants limit on event '550e8400-e29b-41d4-a716-446655440000'"
                            }"""
                        ),
                        ExampleObject(
                            name = "Пользователь уже зарегистрирван",
                            value = """{
                                "code": 400,
                                "message": "User 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11' is already registered to event '550e8400-e29b-41d4-a716-446655440000'"
                            }"""
                        )
                    ]
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Мероприятие не найдено",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponseDto::class),
                    examples = [
                        ExampleObject(
                            name = "Мероприятие не найдено",
                            value = """{
                                "code": 404,
                                "message": "Event with id '550e8400-e29b-41d4-a716-446655440000' doesn't exist!"
                            }"""
                        )
                    ]
                )]
            )
        ]
    )
    fun registerToEvent(@PathVariable eventID: UUID): ResponseEntity<EventParticipantDto>
}