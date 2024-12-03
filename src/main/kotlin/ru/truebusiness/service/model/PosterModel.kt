package ru.truebusiness.service.model

import lombok.Data
import java.time.LocalDateTime

@Data
class PosterModel {
    val posterName: String? = null
    val dateFrom: LocalDateTime? = null
    val dateTo: LocalDateTime? = null
    val title: String? = null
    val content: String? = null
}