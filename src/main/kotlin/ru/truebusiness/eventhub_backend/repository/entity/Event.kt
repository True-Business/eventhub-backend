package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "events")
class Event() {

    @Id
    var id: UUID = UUID.randomUUID()

    var name: String = ""

    // TODO: startTime лишнее
    var startDate: LocalDateTime? = null
    var startTime: LocalDateTime? = null
    var endTime: LocalDateTime? = null

    var updatedAt: LocalDateTime? = LocalDateTime.now()

    var organizerId: UUID? = null
    var organizationId: UUID? = null
    var eventCategory: Int? = null

    var address: String? = null
    var route: String? = null
    var description: String? = null

    var isFree: Boolean? = null
    var price: Double? = null
    var isOpen: Boolean? = null

    var eventStatus: String? = null
    var city: String? = null
    var isWithRegister: Boolean? = null
    var peopleLimit: Int? = null

    // TODO: registerEndDate лишнее, можно всё в registerEndTime запихать и назвать как registerEndDate
    var registerEndDate: LocalDate? = null
    var registerEndTime: LocalDateTime? = null
}