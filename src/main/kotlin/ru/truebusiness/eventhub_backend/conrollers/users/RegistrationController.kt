package ru.truebusiness.eventhub_backend.conrollers.users

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.truebusiness.eventhub_backend.conrollers.dto.ErrorResponseDto
import ru.truebusiness.eventhub_backend.conrollers.dto.users.ConfirmForgotPasswordRequest
import ru.truebusiness.eventhub_backend.conrollers.dto.users.RegistrationResponseDto
import ru.truebusiness.eventhub_backend.conrollers.dto.users.UserCredentialsRegistrationDto
import ru.truebusiness.eventhub_backend.conrollers.dto.users.UserInfoRegistrationDto

@Tag(
    name = "Регистрация пользователей",
    description = "API для регистрации пользователей"
)
@RequestMapping("/api/v1/auth")
interface RegistrationController {
    @Operation(
        summary = "Регистрация нового пользователя",
        description = "Создаёт нового пользователя с указанным email и паролем. Возвращает статус PENDING до подтверждения email.",
        responses = [ApiResponse(
            responseCode = "201",
            description = "Пользователь успешно зарегистрирован",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = RegistrationResponseDto::class)
            )],
        ), ApiResponse(
            responseCode = "409",
            description = "Пользователь с таким email уже существует",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class)
            )],
        )]
    )
    @PostMapping
    fun preRegister(
        @RequestBody(
            description = "Данные для регистрации",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = UserCredentialsRegistrationDto::class),
                examples = [ExampleObject(
                    name = "Пример запроса",
                    value = """{
                        "email": "user@example.com",
                        "password": "secure_password123"
                    }""",
                )]
            )]
        )
        @org.springframework.web.bind.annotation.RequestBody
        dto: UserCredentialsRegistrationDto
    ): ResponseEntity<RegistrationResponseDto>

    @Operation(
        summary = "Отправка кода подтверждения на email", description = """
        Генерирует и отправляет одноразовый код подтверждения на email пользователя.
        Требуется для завершения регистрации (подтверждения email).
    """, responses = [ApiResponse(
            responseCode = "200",
            description = "Код успешно отправлен на email",
            content = [Content(
                mediaType = "application/json", examples = [ExampleObject(
                    name = "Пример успешного ответа",
                    value = """{}""",
                    summary = "Пустой ответ при успешной отправке",
                )]
            )]
        ), ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class),
                examples = [ExampleObject(
                    name = "Пример ошибки",
                    value = """{
                            "code": 404,
                            "message": "User with id '550e8400-e29b-41d4-a716-446655440000' not found"
                        }""",
                )],
            )]
        )]
    )
    @PutMapping("/send-code/{userId}")
    fun sendConfirmationCode(
        @Parameter(
            description = "UUID пользователя в формате 8-4-4-4-12",
            example = "550e8400-e29b-41d4-a716-446655440000",
            schema = Schema(
                type = "string",
                format = "uuid",
                example = "550e8400-e29b-41d4-a716-446655440000"
            ),
        )
        @PathVariable
        userId: UUID
    )

    @Operation(
        summary = "Проверка кода подтверждения регистрации", description = """
        Проверяет введенный пользователем код подтверждения. 
        При успешной проверке завершает регистрацию, помечая пользователя как подтвержденного.
    """, responses = [ApiResponse(
            responseCode = "200",
            description = "Код верен, регистрация завершена успешно",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = RegistrationResponseDto::class),
                examples = [ExampleObject(
                    name = "Успешное подтверждение",
                    value = """{
                            "id": "550e8400-e29b-41d4-a716-446655440000",
                            "registrationDate": "2023-10-05T12:34:56.789Z",
                            "status": "SUCCESS"
                        }""",
                )]
            )]
        ), ApiResponse(
            responseCode = "400",
            description = "Неверный код или код просрочен",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class),
                examples = [ExampleObject(
                    name = "Неверный код",
                    value = """{
                            "code": 400,
                            "message": "Invalid confirmation code: '123456'"
                        }""",
                    summary = "Код не найден в системе",
                ), ExampleObject(
                    name = "Просроченный код",
                    value = """{
                            "code": 400,
                            "message": "Confirmation code '789012' has expired"
                        }""",
                    summary = "Срок действия кода истек",
                )]
            )]
        )]
    )
    @PutMapping("/check-code/{code}")
    fun verifyConfirmationCode(
        @PathVariable
        code: String
    ): ResponseEntity<RegistrationResponseDto>

    @Operation(
        summary = "Добавление профильной информации пользователя",
        description = """
        Завершает процесс регистрации, добавляя имя пользователя и уникальный короткий идентификатор.
        После успешного выполнения пользователь считается полностью зарегистрированным.
    """,
        responses = [ApiResponse(
            responseCode = "200",
            description = "Профильная информация успешно добавлена",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = RegistrationResponseDto::class),
                examples = [ExampleObject(
                    name = "Успешное завершение регистрации",
                    value = """{
                            "id": "550e8400-e29b-41d4-a716-446655440000",
                            "registrationDate": "2023-10-05T12:34:56.789Z",
                            "status": "SUCCESS"
                        }""",
                )]
            )]
        ), ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class),
                examples = [
                    ExampleObject(
                        name = "Пользователь не найден",
                        value = """{
                            "code": 404,
                            "message": "User with id '550e8400-e29b-41d4-a716-446655440000' not found"
                        }""",
                    ),
                ]
            )]
        ), ApiResponse(
            responseCode = "409",
            description = "Короткий идентификатор уже занят",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class),
                examples = [
                    ExampleObject(
                        name = "Конфликт идентификатора", value = """{
                            "code": 409,
                            "message": "User with shortId 'johndoe' already exists"
                        }"""
                    ),
                ]
            )]
        )]
    )
    @PostMapping("/add-info")
    fun postRegister(
        @RequestBody(
            description = "Данные для завершения регистрации",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = UserInfoRegistrationDto::class),
                examples = [
                    ExampleObject(
                        name = "Пример запроса",
                        value = """{
                        "username": "John Doe",
                        "shortId": "johndoe123"
                    }""",
                        summary = "Корректный запрос с валидными данными",
                    ),
                ],
            )]
        )
        @org.springframework.web.bind.annotation.RequestBody
        registrationDto: UserInfoRegistrationDto
    ): ResponseEntity<RegistrationResponseDto>


    @Operation(summary = "Завершения восстановления пароля")
    @PostMapping("/forgot-password/confirm")
    fun confirmForgotPassword(
        @RequestBody(
            description = "Данные для завершения восстановления пароля",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ConfirmForgotPasswordRequest::class),
                examples = [
                    ExampleObject(
                        name = "Пример запроса",
                        value = """{
                        "code": "1234",
                        "password": "SecurePass1234!"
                    }""",
                    ),
                ],
            )]
        )
        @org.springframework.web.bind.annotation.RequestBody
        request: ConfirmForgotPasswordRequest
    ): ResponseEntity<Void>
}