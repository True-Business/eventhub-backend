package ru.truebusiness.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Poster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var from: LocalDateTime? = null
    var to: LocalDateTime? = null
    var title: String? = null
    var content: String? = null
}