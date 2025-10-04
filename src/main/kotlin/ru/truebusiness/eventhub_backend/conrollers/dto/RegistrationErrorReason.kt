package ru.truebusiness.eventhub_backend.conrollers.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "Причины ошибок регистрации",
    example = "USER_ALREADY_REGISTERED"
)
enum class RegistrationErrorReason {
    @Schema(description = "Пользователь не найден в системе")
    USER_NOT_FOUND,

    @Schema(description = "Введен неверный код подтверждения")
    INCORRECT_CONFIRMATION_CODE,

    @Schema(description = "Код подтвержения истек")
    USER_ALREADY_REGISTERED,

    @Schema(description = "Пользователь с таким email уже зарегистрирован")
    CONFIRMATION_CODE_EXPIRED,

    @Schema(description = "Email не указан в запросе")
    MISSING_EMAIL,

    @Schema(description = "Короткий идентификатор (shortId) уже используется другим пользователем")
    SHORT_ID_ALREADY_USED,
}