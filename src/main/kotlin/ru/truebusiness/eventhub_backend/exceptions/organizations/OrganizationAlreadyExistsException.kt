package ru.truebusiness.eventhub_backend.exceptions.organizations

class OrganizationAlreadyExistsException private constructor(
    message: String
) : OrganizationsException(message) {
    companion object {
        fun withName(name: String): OrganizationAlreadyExistsException {
            return OrganizationAlreadyExistsException(
                "Organization with name '${name}' already exists"
            )
        }
    }
}