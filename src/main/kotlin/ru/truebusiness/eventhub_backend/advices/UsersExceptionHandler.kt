package ru.truebusiness.eventhub_backend.advices

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.truebusiness.eventhub_backend.conrollers.dto.ErrorResponseDto
import ru.truebusiness.eventhub_backend.exceptions.users.InvalidConfirmationCode
import ru.truebusiness.eventhub_backend.exceptions.users.UserAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.users.UserNotFoundException

@RestControllerAdvice
class UsersExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserNotFound(ex: UserNotFoundException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.NOT_FOUND.value(),
            message = ex.message
        )
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleUserAlreadyExists(ex: UserAlreadyExistsException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.CONFLICT.value(),
            message = ex.message
        )
    }

    @ExceptionHandler(InvalidConfirmationCode::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidConfirmationCode(ex: InvalidConfirmationCode): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.BAD_REQUEST.value(),
            message = ex.message
        )
    }
}