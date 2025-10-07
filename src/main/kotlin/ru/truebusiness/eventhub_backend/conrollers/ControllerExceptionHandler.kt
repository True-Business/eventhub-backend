package ru.truebusiness.eventhub_backend.conrollers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.truebusiness.eventhub_backend.conrollers.dto.ErrorResponseDto
import ru.truebusiness.eventhub_backend.exceptions.*
import ru.truebusiness.eventhub_backend.logger

@RestControllerAdvice
class ControllerExceptionHandler {
    private val log by logger()

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleAllUncaughtException(ex: Exception): ErrorResponseDto {
        log.error(ex.message)
        return ErrorResponseDto(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = "An unexpected error occurred"
            // do not expose internal errors to user, they might contain sensitive data
        )
    }

    @ExceptionHandler(OrganizationAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleOrganizationAlreadyExists(ex: OrganizationAlreadyExistsException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.CONFLICT.value(),
            message = ex.message ?: "Organization already exists"
        )
    }

    @ExceptionHandler(OrganizationNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleOrganizationNotFound(ex: OrganizationNotFoundException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "Organization not found"
        )
    }

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserNotFound(ex: UserNotFoundException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "User not found"
        )
    }
}