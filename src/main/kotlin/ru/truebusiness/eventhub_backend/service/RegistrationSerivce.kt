package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.mapper.UserMapper
import ru.truebusiness.eventhub_backend.repository.UserCredentialsRepository
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.entity.UserCredentials
import ru.truebusiness.eventhub_backend.service.model.UserRegistrationModel

@Service
class RegistrationSerivce(
    val userRepository: UserRepository,
    val userCredentialsRepository: UserCredentialsRepository,
    val userMapper: UserMapper,
    val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun registerUser(userRegistrationModel: UserRegistrationModel) {
        userRegistrationModel.email?.let { userEmail ->
            if (userCredentialsRepository.findByEmail(userEmail) != null) {
                throw IllegalArgumentException("User email already registered!")
            }
        } ?: throw IllegalArgumentException("User email doesn't exist!")

        val newUser = userRepository.save(userMapper.userModelToUserEntity(userRegistrationModel))
        val credentials = UserCredentials()
        credentials.email = userRegistrationModel.email
        credentials.password = passwordEncoder.encode(userRegistrationModel.password)
        credentials.user = newUser
        userCredentialsRepository.save(credentials)
    }
}