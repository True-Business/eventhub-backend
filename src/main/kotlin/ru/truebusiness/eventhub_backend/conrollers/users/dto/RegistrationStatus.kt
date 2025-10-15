package ru.truebusiness.eventhub_backend.conrollers.users.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "Статус процесса регистрации",
    example = "SUCCESS",
    allowableValues = ["PENDING", "SUCCESS", "ERROR"]
)
enum class RegistrationStatus {
    /**
     * Регистрация не завершена (ожидает подтверждения кодом)
     */
    @Schema(description = "Регистрация подтверждена кодом, но не завершена")
    PENDING,

    /**
     * Регистрация успешно завершена
     */
    @Schema(description = "Регистрация успешно завершена")
    SUCCESS,

    /**
     * Ошибка в процессе регистрации
     */
    @Schema(description = "Ошибка в процессе регистрации")
    ERROR,
}