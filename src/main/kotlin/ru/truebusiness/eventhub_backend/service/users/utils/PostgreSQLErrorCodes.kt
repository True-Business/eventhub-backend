package ru.truebusiness.eventhub_backend.service.users.utils

// https://www.postgresql.org/docs/current/errcodes-appendix.html
enum class PostgreSQLErrorCodes(val code : String) {
    UniqueConstraint("23505")
}