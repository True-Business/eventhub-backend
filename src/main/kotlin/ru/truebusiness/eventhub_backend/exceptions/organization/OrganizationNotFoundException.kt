package ru.truebusiness.eventhub_backend.exceptions.organization

import java.util.UUID

class OrganizationNotFoundException private constructor(
    message: String
) : OrganizationsException(message) {
    companion object {
        fun withName(name: String): OrganizationNotFoundException {
            return OrganizationNotFoundException(
                "organization with name $name not found"
            )
        }

        fun withID(uuid: UUID): OrganizationNotFoundException {
            return OrganizationNotFoundException(
                "organization with id: $uuid does not exist"
            )
        }
    }
}