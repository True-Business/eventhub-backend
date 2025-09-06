package ru.truebusiness.eventhub_backend.conrollers.dto

enum class RegistrationErrorReason {
    USER_NOT_FOUND,
    INCORRECT_CONFIRMATION_CODE,
    USER_ALREADY_REGISTERED,
    MISSING_EMAIL,
    SHORT_ID_ALREADY_USED,
}