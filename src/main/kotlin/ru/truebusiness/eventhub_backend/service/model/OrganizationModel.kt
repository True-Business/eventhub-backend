package ru.truebusiness.eventhub_backend.service.model

import java.util.*

class OrganizationModel {
    val name: String = ""
    val description: String = ""

    val address: String? = null
    val picture: String? = null

    val isVerified: Boolean = false
    var creatorId: UUID? = null
}