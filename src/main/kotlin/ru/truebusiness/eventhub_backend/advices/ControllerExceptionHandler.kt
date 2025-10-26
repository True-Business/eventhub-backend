package ru.truebusiness.eventhub_backend.advices

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.truebusiness.eventhub_backend.conrollers.dto.ErrorResponseDto
import ru.truebusiness.eventhub_backend.exceptions.organization.OrganizationAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.organization.OrganizationNotFoundException

@RestControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(OrganizationAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleOrganizationAlreadyExists(ex: OrganizationAlreadyExistsException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.CONFLICT.value(),
            message = ex.message
        )
    }

    @ExceptionHandler(OrganizationNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleOrganizationNotFound(ex: OrganizationNotFoundException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.NOT_FOUND.value(),
            message = ex.message
        )
    }

    @ExceptionHandler(NotImplementedException::class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    fun handleNotImplemented(ex: NotImplementedException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.NOT_IMPLEMENTED.value(),
            message = ex.message ?: "Not implemented"
        )
    }
}