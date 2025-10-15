package ru.truebusiness.eventhub_backend.conrollers.users

import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.users.dto.RegistrationResponseDto
import ru.truebusiness.eventhub_backend.conrollers.users.dto.UserCredentialsRegistrationDto
import ru.truebusiness.eventhub_backend.conrollers.users.dto.UserInfoRegistrationDto
import ru.truebusiness.eventhub_backend.service.users.RegistrationService

@RestController
class RegistrationControllerImpl(
    val registrationService: RegistrationService
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
}