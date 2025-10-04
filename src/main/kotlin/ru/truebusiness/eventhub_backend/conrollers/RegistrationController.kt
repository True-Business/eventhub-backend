package ru.truebusiness.eventhub_backend.conrollers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.RegistrationResponseDto
import ru.truebusiness.eventhub_backend.conrollers.dto.RegistrationStatus
import ru.truebusiness.eventhub_backend.conrollers.dto.UserCredentialsRegistrationDto
import ru.truebusiness.eventhub_backend.conrollers.dto.UserInfoRegistrationDto
import ru.truebusiness.eventhub_backend.service.RegistrationService

@RestController
@RequestMapping("/api/v1/auth")
@Tag(
    name = "Регистрация пользователей",
    description = "API для регистрации пользователей"
)
class RegistrationController(
    val registrationService: RegistrationService
) {
    @Operation(
        summary = "Предварительная регистрация пользователя",
        description = "Создает предварительную запись регистрации с email и паролем. " + "Генерирует идентификатор регистрации и отправляет код подтверждения на email.",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Учетные данные пользователя для регистрации",
            required = true,
            content = [Content(schema = Schema(implementation = UserCredentialsRegistrationDto::class))]
        ),
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Пользователь успешно предварительно зарегистрирован",
                content = [
                    Content(
                        schema = Schema(implementation = RegistrationResponseDto::class),
                        examples = [
                            ExampleObject(
                                name = "Успешная предварительная регистрация",
                                summary = "Создана предварительная запись, ожидает подтверждения email",
                                value = """{
                            "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                            "registrationDate": "2023-10-25T14:30:00",
                            "status": "PENDING"
                        }""",
                            ),
                        ],
                    ),
                ],
            ),
        ]
    )
    @PostMapping
    fun preRegister(
        @RequestBody
        dto: UserCredentialsRegistrationDto
    ): ResponseEntity<RegistrationResponseDto> {
        val response = registrationService.preRegisterUser(
            dto.email, dto.password
        )
        val statusCode = when (response.status) {
            RegistrationStatus.ERROR -> HttpStatus.CONFLICT
            else -> HttpStatus.OK
        }
        return ResponseEntity(response, statusCode)
    }

    @Operation(
        summary = "Повторная отправка кода подтверждения",
        description = "Отправляет новый код подтверждения на email пользователя. " +
                "Используется для повторной отправки кода в случае его утери.",
        parameters = [
            Parameter(
                name = "userId",
                description = "Идентификатор пользователя из предыдущего этапа регистрации",
                example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                required = true
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Код успешно отправлен"
            ),
        ]
    )
    @PutMapping("/send-code/{userId}")
    fun sendConfirmationCode(
        @PathVariable
        userId: String
    ) {
        val res: Pair<String, String?> =
            registrationService.createConfirmationCode(userId)
        registrationService.sendCodeViaEmail(res.first, res.second)
    }

    @Operation(
        summary = "Проверка кода подтверждения",
        description = "Проверяет введенный пользователем код подтверждения. ",
        parameters = [
            Parameter(
                name = "code",
                description = "4-значный код подтверждения, отправленный на email",
                example = "1234",
                required = true
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Код верен, регистрация подтверждена",
                content = [Content(
                    schema = Schema(implementation = RegistrationResponseDto::class),
                    examples = [
                        ExampleObject(
                            name = "Успешная проверка кода",
                            summary = "Код верен, регистрация подтверждена",
                            value = """{
                            "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                            "registrationDate": "2023-10-25T14:30:00",
                            "status": "SUCCESS"
                        }"""
                        )
                    ]
                )]
            ),
        ]
    )
    @PutMapping("/check-code/{code}")
    fun verifyConfirmationCode(
        @PathVariable
        code: String
    ): ResponseEntity<RegistrationResponseDto> {
        val result = registrationService.verifyRegistrationCode(code)
        val statusCode = when (result.status) {
            RegistrationStatus.ERROR -> HttpStatus.BAD_REQUEST
            else -> HttpStatus.OK
        }
        return ResponseEntity(result, statusCode)
    }

    @Operation(
        summary = "Завершение регистрации",
        description = "Добавляет дополнительные данные пользователя после успешного подтверждения email. " +
                "Завершает процесс регистрации, делая пользователя полностью активным.",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Дополнительная информация о пользователе",
            required = true,
            content = [Content(schema = Schema(implementation = UserInfoRegistrationDto::class))]
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Регистрация успешно завершена",
                content = [Content(
                    schema = Schema(implementation = RegistrationResponseDto::class),
                    examples = [
                        ExampleObject(
                            name = "Успешное завершение регистрации",
                            summary = "Пользователь полностью зарегистрирован",
                            value = """{
                            "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                            "registrationDate": "2023-10-25T14:30:00",
                            "status": "SUCCESS"
                        }"""
                        )
                    ]
                )]
            ),
        ]
    )
    @PostMapping("/add-info")
    fun postRegister(
        @RequestBody
        registrationDto: UserInfoRegistrationDto
    ): ResponseEntity<RegistrationResponseDto> {
        val response = registrationService.addUserInfo(
            registrationDto.id,
            registrationDto.username,
            registrationDto.shortId
        )
        val statusCode = when (response.status) {
            RegistrationStatus.ERROR -> HttpStatus.BAD_REQUEST
            else -> HttpStatus.OK
        }
        return ResponseEntity(response, statusCode)
    }
}