package ru.truebusiness.eventhub_backend.conrollers.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID

data class UserInfoRegistrationDto(
    @field:Schema(
        description = "Уникальный идентификатор пользователя в системе",
        example = "user123"
    )
    val id: String,

    @field:Schema(description = "Имя пользователя", example = "john_doe")
    val username: String,

    @field:Schema(
        description = "Короткий идентификатор для отображения",
        example = "JD123"
    )
    val shortId: String,
)

data class UserCredentialsRegistrationDto(
    @field:Schema(description = "Пароль", example = "securePass123!")
    val password: String,

    @field:Schema(
        description = "Электронная почта", example = "user@example.com"
    )
    val email: String,
)

data class RegistrationResponseDto(
    @field:Schema(
        description = "Уникальный идентификатор регистрации",
        example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"
    )
    val id: UUID?,
    val registrationDate: Instant?,
    @field:Schema(
        description = """
            Статус регистрации:
            * PENDING - регистрация подтверждена, но не завершена
            * SUCCESS - регистрация успешно завершена
            * ERROR - ошибка в процессе регистрации
        """,
        allowableValues = ["PENDING", "SUCCESS", "ERROR"],
        example = "SUCCESS"
    )
    override val status: RegistrationStatus,
    @field:Schema(
        description = """
            Причина ошибки (присутствует только при статусе ERROR). 
            Возможные значения:
            * USER_NOT_FOUND - пользователь не найден
            * INCORRECT_CONFIRMATION_CODE - неверный код подтверждения
            * USER_ALREADY_REGISTERED - пользователь уже зарегистрирован
            * MISSING_EMAIL - отсутствует email в запросе
            * SHORT_ID_ALREADY_USED - короткий идентификатор уже занят
        """, example = "USER_ALREADY_REGISTERED", nullable = true
    )
    override val reason: String? = null,
) : BaseStatusDto<RegistrationStatus>(status, reason) {

    companion object {
        fun error(reason: RegistrationErrorReason): RegistrationResponseDto {
            return RegistrationResponseDto(
                id = null,
                registrationDate = null,
                status = RegistrationStatus.ERROR,
                reason = reason.name,
            )
        }

        fun pending(
            id: UUID?, registrationDate: Instant?
        ): RegistrationResponseDto {
            return RegistrationResponseDto(
                id = id,
                registrationDate = registrationDate,
                status = RegistrationStatus.PENDING,
            )
        }

        fun success(
            id: UUID?, registrationDate: Instant?
        ): RegistrationResponseDto {
            return RegistrationResponseDto(
                id = id,
                registrationDate = registrationDate,
                status = RegistrationStatus.SUCCESS,
            )
        }
    }
}
