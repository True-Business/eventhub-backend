package ru.truebusiness.eventhub_backend.service.model

import java.time.LocalDateTime

class UserRegistrationModel {
    var shortId: String? = null
    var username: String? = null
    var password: String? = null
    var email: String? = null
    var registrationDate: LocalDateTime? = null
}