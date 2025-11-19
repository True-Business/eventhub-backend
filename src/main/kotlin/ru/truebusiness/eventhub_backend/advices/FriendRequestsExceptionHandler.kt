package ru.truebusiness.eventhub_backend.advices

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.truebusiness.eventhub_backend.conrollers.dto.ErrorResponseDto
import ru.truebusiness.eventhub_backend.exceptions.friends.FriendRequestAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.friends.SelfFriendRequestException

@RestControllerAdvice
class FriendRequestsExceptionHandler {

    @ExceptionHandler(FriendRequestAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleFriendRequestAlreadyExists(ex: FriendRequestAlreadyExistsException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.CONFLICT.value(),
            message = ex.message
        )
    }

    @ExceptionHandler(SelfFriendRequestException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleSelfFriendRequest(ex: SelfFriendRequestException): ErrorResponseDto {
        return ErrorResponseDto(
            code = HttpStatus.CONFLICT.value(),
            message = ex.message
        )
    }
}