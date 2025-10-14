package ru.truebusiness.eventhub_backend.exceptions.organizations

open class OrganizationsException(
    override val message: String
) : RuntimeException(message)