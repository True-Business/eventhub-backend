package ru.truebusiness.eventhub_backend.exceptions.organization

open class OrganizationsException(
    override val message: String
) : RuntimeException(message)