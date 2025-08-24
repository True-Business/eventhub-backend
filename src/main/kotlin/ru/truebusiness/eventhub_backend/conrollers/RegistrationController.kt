package ru.truebusiness.eventhub_backend.conrollers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.RegistrationDto
import ru.truebusiness.eventhub_backend.conrollers.dto.RegistrationResponse
import ru.truebusiness.eventhub_backend.mapper.UserMapper
import ru.truebusiness.eventhub_backend.service.RegistrationService

@RestController
@RequestMapping("/api/v1/auth")
class RegistrationController(
    val userMapper: UserMapper,
    val registrationService: RegistrationService
) {
    @PostMapping
    fun preRegister(@RequestBody registrationDto: RegistrationDto): ResponseEntity<RegistrationResponse> {
        val response = registrationService
            .preRegisterUser(userMapper.registrationDtoToUserRegistrationModel(registrationDto))
        return ResponseEntity.ok(response)
    }

    @PutMapping("/send-code/{userId}")
    fun sendConfirmationCode(@PathVariable userId: String) {
        val res: Pair<String, String?> = registrationService.sendConfirmationCode(userId)
        registrationService.sendCodeViaEmail(res.first, res.second)
    }

    @PutMapping("check-code/{code}")
    fun verifyConfirmationCode(@PathVariable code: String) {
        registrationService.verifyRegistrationCode(code)
    }
}