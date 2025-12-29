package ru.truebusiness.eventhub_backend.service.model;

import java.time.Instant
import java.util.UUID

class CreateEventModel(
    var name: String,
    var startDateTime: Instant,
    var endDateTime: Instant?,
    var updatedAt: Instant = Instant.now(),
    var organizerId: UUID,
    var organizationId: UUID?,
    var category: EventCategoryModel,
    var address: String,
    var route: String,
    var description: String,
    var price: Double,
    var isOpen: Boolean,
    var status: EventStatusModel,
    var city: String,
    var isWithRegister: Boolean,
    var peopleLimit: Int?,
    var registerEndDateTime: Instant?,
) {
    override fun toString(): String {
        return "CreateEventModel(\n" +
                "   name: $name\n" +
                "   startDateTime: $startDateTime\n" +
                "   endDateTime: $endDateTime\n" +
                "   updatedAt: $updatedAt\n" +
                "   organizerId: $organizerId\n" +
                "   organizationId: $organizationId\n" +
                "   category: ${category.name}\n" +
                "   address: $address\n" +
                "   route: $route\n" +
                "   description: $description\n" +
                "   price: $price\n" +
                "   isOpen: $isOpen\n" +
                "   status: ${status.name}\n" +
                "   city: $city\n" +
                "   isWithRegister: $isWithRegister\n" +
                "   peopleLimit: $peopleLimit\n" +
                "   registerEndDateTime: $registerEndDateTime\n" +
                ")"
    }
}
