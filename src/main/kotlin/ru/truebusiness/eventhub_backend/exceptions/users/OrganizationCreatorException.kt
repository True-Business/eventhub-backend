package ru.truebusiness.eventhub_backend.exceptions.users

import java.util.UUID

class OrganizationCreatorException private constructor(
    message: String
) : UsersException(message) {

    companion object {
        fun withId(id: UUID): OrganizationCreatorException {
            return OrganizationCreatorException("user with id: $id is head of the organization")
        }
    }
}