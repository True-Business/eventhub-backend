package ru.truebusiness.eventhub_backend.service.model

import java.util.UUID

class OrganizationAdminModel(
    var id: UUID,
    var organizationId: UUID,
    var adminId: UUID
)