package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.RegistrationResponse
import ru.truebusiness.eventhub_backend.exceptions.UserNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.UserMapper
import ru.truebusiness.eventhub_backend.repository.ConfirmationCodeRepository
import ru.truebusiness.eventhub_backend.repository.UserCredentialsRepository
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.entity.ConfirmationCode
import ru.truebusiness.eventhub_backend.repository.entity.UserCredentials
import ru.truebusiness.eventhub_backend.service.model.UserRegistrationModel
import java.util.*

@Service
class RegistrationService(
    private val userRepository: UserRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val confirmationCodeRepository: ConfirmationCodeRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService
) {
    private val log by logger()

    /**
     * Метод предвариетльно регистрирует пользователя: создаётся запись о пользователе в бд, но с не подтверждённым
     * статусом.
     *
     * @param userRegistrationModel - модель, содержащая данные для регистрации пользователя.
     * @return идентификатор созданного пользователя и дату регистрации.
     */
    @Transactional
    fun preRegisterUser(userRegistrationModel: UserRegistrationModel): RegistrationResponse {
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
        log.info("User created successfully! Warning: user is not confirmed!")

        return RegistrationResponse(
            newUser.id,
            newUser.registrationDate
        )
    }

    /**
     * Метод создаёт код для подтверждения регистрации пользователя и отпраляет его на почту, указнную пользвоателем.
     *
     * @param userId - идентификатор пользователя.
     */
    @Transactional
    fun sendConfirmationCode(userId: String): Pair<String, String?> {
        return createConfirmationCode(userId)
    }

    fun sendCodeViaEmail(code: String, email: String?) {
        emailService.sendConfirmationCode(code, email)
    }

    @Transactional
    fun verifyRegistrationCode(code: String) {
        confirmationCodeRepository.findByCode(code)?.let { confirmationCode ->
            log.info("Confirmation code found! Updating user status...")
            val user = confirmationCode.user
            user?.let {
                it.isConfirmed = true
                userRepository.save(it)
                log.info("User ${it.id} status successfully updated!")
                confirmationCodeRepository.delete(confirmationCode)
            } ?: log.error("Could not update user confirmation status!")
        } ?: throw IllegalArgumentException("Could not find confirmation code $code!")
    }

    /**
     * Метод создаёт код для подтверждения регистрации пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return пару - код и почту пользователя.
     */
    private fun createConfirmationCode(userId: String): Pair<String, String?> {
        log.info("Creating confirmation code for user with id: $userId")

        userRepository.findUserById(UUID.fromString(userId))?.let { user ->
            val confirmationCode = ConfirmationCode()
            confirmationCode.user = user
            confirmationCode.code = UUID.randomUUID().toString()
            val savedCode = confirmationCodeRepository.save(confirmationCode)
            log.info("Confirmation code for user with id: $userId was saved! Code: $savedCode")

            return Pair(savedCode.id.toString(), user.credentials?.email)
        } ?: throw UserNotFoundException("User with id $userId doesn't exist!", null)
    }
}