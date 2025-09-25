package ru.truebusiness.eventhub_backend.conrollers.dto

open class BaseStatusDto<T : Enum<T>>(
    open val status: T,
    open val reason: String?
)