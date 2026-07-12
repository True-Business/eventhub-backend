package ru.truebusiness.eventhub_backend.advices.base

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.truebusiness.eventhub_backend.conrollers.dto.ErrorResponseDto

@RestControllerAdvice
class BaseControllerExceptionHandler {
    companion object {
        private val log = KotlinLogging.logger {}
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleAllUncaughtException(ex: Exception): ErrorResponseDto {
        log.error(ex.message)
        return ErrorResponseDto(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = "An unexpected error occurred"
        )
    }
}
