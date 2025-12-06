package ru.truebusiness.eventhub_backend.conrollers.dto.users

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID
import ru.truebusiness.eventhub_backend.conrollers.dto.BaseStatusDto

data class UserInfoRegistrationDto(
    @field:Schema(
        description = "Уникальный идентификатор пользователя в системе",
        example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"
    )
    val id: UUID,

    @field:Schema(description = "Имя пользователя", example = "john_doe")
    val username: String,

    @field:Schema(
        description = "Короткий идентификатор для отображения",
        example = "@JD123"
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
    @param:Schema(
        description = "Уникальный идентификатор регистрации",
        example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"
    )
    val id: UUID,

    val registrationDate: Instant,
    @param:Schema(
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
) : BaseStatusDto<RegistrationStatus> {

    companion object {
        fun pending(
            id: UUID, registrationDate: Instant
        ): RegistrationResponseDto {
            return RegistrationResponseDto(
                id = id,
                registrationDate = registrationDate,
                status = RegistrationStatus.PENDING,
            )
        }

        fun success(
            id: UUID, registrationDate: Instant
        ): RegistrationResponseDto {
            return RegistrationResponseDto(
                id = id,
                registrationDate = registrationDate,
                status = RegistrationStatus.SUCCESS,
            )
        }
    }
}

data class ForgotPasswordRequest(
    @field:Schema(
        description = "Электронная почта", example = "user@example.com"
    )
    val email: String
)

data class ConfirmForgotPasswordRequest(
    @field:Schema(description = "Код подтверждения", example = "1234")
    val code: String,

    @field:Schema(description = "Новый пароль", example = "securePass123!")
    val password: String
)
