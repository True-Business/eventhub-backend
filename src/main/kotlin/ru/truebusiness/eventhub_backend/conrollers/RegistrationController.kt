package ru.truebusiness.eventhub_backend.conrollers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.RegistrationResponseDto
import ru.truebusiness.eventhub_backend.conrollers.dto.UserCredentialsRegistrationDto
import ru.truebusiness.eventhub_backend.conrollers.dto.UserInfoRegistrationDto
import ru.truebusiness.eventhub_backend.service.RegistrationService

@RestController
@RequestMapping("/api/v1/auth")
class RegistrationController(
    val registrationService: RegistrationService
) {
    @PostMapping
    fun preRegister(@RequestBody dto: UserCredentialsRegistrationDto):
            ResponseEntity<RegistrationResponseDto> {
        val response = registrationService.preRegisterUser(dto.email, dto.password)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/send-code/{userId}")
    fun sendConfirmationCode(@PathVariable userId: String) {
        val res: Pair<String, String?> = registrationService.createConfirmationCode(userId)
        registrationService.sendCodeViaEmail(res.first, res.second)
    }

    @PutMapping("/check-code/{code}")
    fun verifyConfirmationCode(@PathVariable code: String): ResponseEntity<RegistrationResponseDto> {
        val result = registrationService.verifyRegistrationCode(code)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/add-info")
    fun postRegister(@RequestBody registrationDto: UserInfoRegistrationDto): ResponseEntity<RegistrationResponseDto> {
        val response =
            registrationService.addUserInfo(registrationDto.id, registrationDto.username, registrationDto.shortId)
        return ResponseEntity.ok(response)
    }
}