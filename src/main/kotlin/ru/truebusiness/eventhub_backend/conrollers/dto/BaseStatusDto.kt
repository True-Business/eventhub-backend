package ru.truebusiness.eventhub_backend.conrollers.dto

open class BaseStatusDto (
    open val status: String,
    open val reason: String?
)