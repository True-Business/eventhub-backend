package ru.truebusiness.eventhub_backend.advices

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.truebusiness.eventhub_backend.conrollers.dto.ErrorResponseDto
import ru.truebusiness.eventhub_backend.exceptions.OrganizationAlreadyExistsException
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
}