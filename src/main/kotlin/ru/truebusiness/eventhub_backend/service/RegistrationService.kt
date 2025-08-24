package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.RegistrationResponse
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.UserMapper
import ru.truebusiness.eventhub_backend.repository.UserCredentialsRepository
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.entity.UserCredentials
import ru.truebusiness.eventhub_backend.service.model.UserRegistrationModel

@Service
class RegistrationService(
    private val userRepository: UserRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
) {
    private val log by logger()

    @Transactional
    fun registerUser(userRegistrationModel: UserRegistrationModel): RegistrationResponse {
        userRegistrationModel.email?.let { userEmail ->
            if (userCredentialsRepository.findByEmail(userEmail) != null) {
                throw IllegalArgumentException("User email already registered!")
            }
        } ?: throw IllegalArgumentException("User email doesn't exist!")
        log.info("Started registration of new user...")

        val newUser = userRepository.save(userMapper.userModelToUserEntity(userRegistrationModel))
        log.info("New user registered! User id = ${newUser.id}")

        log.info("Saving new user credentials...")
        val credentials = UserCredentials()
        credentials.email = userRegistrationModel.email
        credentials.password = passwordEncoder.encode(userRegistrationModel.password)
        credentials.user = newUser
        userCredentialsRepository.save(credentials)
        log.info("Credentials for user ${newUser.id} saved!")
        log.info("User created successfully!")

        return RegistrationResponse(
            newUser.id,
            newUser.registrationDate
        )
    }
}