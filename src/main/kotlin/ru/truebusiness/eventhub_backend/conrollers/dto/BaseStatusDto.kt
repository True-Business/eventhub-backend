package ru.truebusiness.eventhub_backend.conrollers.dto

interface BaseStatusDto<T : Enum<T>> {
    val status: T
}