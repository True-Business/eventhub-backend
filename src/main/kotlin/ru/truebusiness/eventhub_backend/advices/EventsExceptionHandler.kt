package ru.truebusiness.eventhub_backend.advices

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.truebusiness.eventhub_backend.conrollers.dto.ErrorResponseDto
import ru.truebusiness.eventhub_backend.exceptions.events.EventNotDraftException
import ru.truebusiness.eventhub_backend.exceptions.events.EventNotFoundException
import ru.truebusiness.eventhub_backend.exceptions.events.RegistrationException

@RestControllerAdvice
class EventsExceptionHandler {

    @ExceptionHandler(EventNotDraftException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleEventNotDraft(ex: EventNotDraftException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.BAD_REQUEST.value(),
            message = ex.message
        )
    }

    @ExceptionHandler(EventNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEventNotFound(ex: EventNotFoundException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.NOT_FOUND.value(),
            message = ex.message
        )
    }

    @ExceptionHandler(RegistrationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleRegistration(ex: RegistrationException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.BAD_REQUEST.value(),
            message = ex.message
        )
    }
}