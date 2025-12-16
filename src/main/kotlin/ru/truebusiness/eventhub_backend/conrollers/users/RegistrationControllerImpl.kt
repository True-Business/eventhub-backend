package ru.truebusiness.eventhub_backend.conrollers.users

import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.UserDto
import ru.truebusiness.eventhub_backend.conrollers.dto.users.ForgotPasswordRequest
import ru.truebusiness.eventhub_backend.conrollers.dto.users.ConfirmForgotPasswordRequest
import ru.truebusiness.eventhub_backend.conrollers.dto.users.RegistrationResponseDto
import ru.truebusiness.eventhub_backend.conrollers.dto.users.UserCredentialsRegistrationDto
import ru.truebusiness.eventhub_backend.conrollers.dto.users.UserInfoRegistrationDto
import ru.truebusiness.eventhub_backend.mapper.UserMapper
import ru.truebusiness.eventhub_backend.service.users.RegistrationService

@RestController
class RegistrationControllerImpl(
    val registrationService: RegistrationService,
    private val userMapper: UserMapper,
) : RegistrationController {
    override fun preRegister(
        dto: UserCredentialsRegistrationDto
    ): ResponseEntity<RegistrationResponseDto> {
        val response = registrationService.preRegisterUser(
            dto.email, dto.password
        )
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    override fun sendConfirmationCode(
        userId: UUID
    ) {
        val res: Pair<String, String> =
            registrationService.createConfirmationCode(userId)
        registrationService.sendCodeViaEmail(res.first, res.second)
    }

    override fun verifyConfirmationCode(
        code: String
    ): ResponseEntity<RegistrationResponseDto> {
        val result = registrationService.verifyRegistrationCode(code)
        return ResponseEntity(result, HttpStatus.OK)
    }

    override fun postRegister(
        registrationDto: UserInfoRegistrationDto
    ): ResponseEntity<RegistrationResponseDto> {
        val response = registrationService.addUserInfo(
            registrationDto.id,
            registrationDto.username,
            registrationDto.shortId
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    override fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): ResponseEntity<Void> {
        registrationService.forgotPassword(forgotPasswordRequest.email)
        return ResponseEntity(HttpStatus.OK)
    }
    
    override fun confirmForgotPassword(request: ConfirmForgotPasswordRequest): ResponseEntity<Void> {
        registrationService.confirmForgotPassword(request.code, request.password)
        return ResponseEntity(HttpStatus.OK)
    }

    override fun login(credentials: UserCredentialsRegistrationDto): ResponseEntity<UserDto> {
        val user = userMapper.userModelToUserDto(
            registrationService.login(credentials.email, credentials.password))
        return ResponseEntity.ok(user)
    }
}