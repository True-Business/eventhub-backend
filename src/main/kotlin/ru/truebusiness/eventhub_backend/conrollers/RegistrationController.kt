package ru.truebusiness.eventhub_backend.conrollers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.RegistrationDto
import ru.truebusiness.eventhub_backend.mapper.UserMapper
import ru.truebusiness.eventhub_backend.service.RegistrationSerivce

@RestController
@RequestMapping("/api/v1/auth")
class RegistrationController(
    val userMapper: UserMapper,
    val registrationSerivce: RegistrationSerivce
) {

    @PostMapping
    fun register(@RequestBody registrationDto: RegistrationDto) {
        registrationSerivce.registerUser(userMapper.registrationDtoToUserRegistrationModel(registrationDto))
    }
}