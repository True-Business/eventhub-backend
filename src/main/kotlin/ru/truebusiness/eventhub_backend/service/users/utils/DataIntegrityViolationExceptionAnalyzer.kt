package ru.truebusiness.eventhub_backend.service.users.utils

import org.postgresql.util.PSQLException
import org.springframework.dao.DataIntegrityViolationException

class DataIntegrityViolationExceptionAnalyzer {
    companion object {
        fun isUniqueViolation(ex: DataIntegrityViolationException): Boolean {
            val psqlException = ex.rootCause as? PSQLException ?: return false
            val serverError = psqlException.serverErrorMessage ?: return false
            val code = serverError.sqlState ?: return false
            return PostgreSQLErrorCodes.UniqueConstraint.code == code
        }
    }
}