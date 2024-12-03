package ru.truebusiness.controller.data.request

import java.time.LocalDateTime

data class NewPosterDto(
    val posterName: String,
    val dateFrom: LocalDateTime,
    val dateTo: LocalDateTime,
    val title: String,
    val content: String
)
