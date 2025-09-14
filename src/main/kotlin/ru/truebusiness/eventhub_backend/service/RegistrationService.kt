package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.RegistrationErrorReason
import ru.truebusiness.eventhub_backend.conrollers.dto.RegistrationResponseDto
import ru.truebusiness.eventhub_backend.exceptions.UserNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.repository.ConfirmationCodeRepository
import ru.truebusiness.eventhub_backend.repository.UserCredentialsRepository
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.entity.ConfirmationCode
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.repository.entity.UserCredentials
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class RegistrationService(
    private val userRepository: UserRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val confirmationCodeRepository: ConfirmationCodeRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService
) {
    companion object {
        private const val CODE_MIN_VALUE: Int = 1000
        private const val CODE_MAX_VALUE: Int = 9999
    }

    private val log by logger()

    /**
     * Метод предвариетльно регистрирует пользователя: создаётся запись о пользователе в бд, но с не подтверждённым
     * статусом.
     *
     * @param email - почта пользователя.
     * @param password - пароль пользователя.
     *
     * @return идентификатор созданного пользователя и дату регистрации.
     */
    @Transactional
    fun preRegisterUser(email: String, password: String): RegistrationResponseDto {
        try {
            log.info("Started registration of new user...")

            val newUser = userRepository.save(User())

            log.info("New user registered! User id = ${newUser.id}")
            log.info("Saving new user credentials...")

            val credentials = UserCredentials().apply {
                this.email = email
                this.password = passwordEncoder.encode(password)
                this.user = newUser
            }
            userCredentialsRepository.save(credentials)

            log.info("Credentials for user ${newUser.id} saved!")
            log.info("User created successfully! Warning: user is not confirmed!")

            return RegistrationResponseDto.pending(newUser.id, newUser.registrationDate)
        } catch (ex: DataIntegrityViolationException) {
            log.error("Couldn't save credentials for user! Email violates unique constraint!")
            return RegistrationResponseDto.error(RegistrationErrorReason.USER_ALREADY_REGISTERED)
        }
    }

    /**
     * Метод выполянет финальный этап регистрации: добавляет юзернейм и корткое имя пользователя.
     *
     * @param id идентификатор пользователя в приложении
     * @param username имя пользователя в приложении
     * @param shortId короткое имя пользователя в приложении
     */
    @Transactional
    fun addUserInfo(id: String, username: String, shortId: String): RegistrationResponseDto {
        val userId = UUID.fromString(id)

        val user = userRepository.findUserById(userId) ?: run {
            log.error("User with id $id not found!")
            return RegistrationResponseDto.error(RegistrationErrorReason.USER_NOT_FOUND)
        }

        userRepository.findUserByShortId(shortId)?.let { existingUser ->
            log.warn("User with shortId $shortId already exists! (User ID: ${existingUser.id})")
            return RegistrationResponseDto.error(RegistrationErrorReason.SHORT_ID_ALREADY_USED)
        }

        user.username = username
        user.shortId = shortId
        val updatedUser = userRepository.save(user)

        return RegistrationResponseDto.success(updatedUser.id, updatedUser.registrationDate)
    }

    /**
     * Метод првоеряет корректность введённого пользователем кода, который был отправлен ему на почту. После проверки
     * код удаляется.
     *
     * @param code введённый пользвоателем код.
     */
    @Transactional
    fun verifyRegistrationCode(code: String): RegistrationResponseDto {
        confirmationCodeRepository.findByCode(code)?.let { confirmationCode ->
            log.info("Confirmation code found! Updating user status...")

            confirmationCode.user?.let {
                it.isConfirmed = true
                userRepository.save(it)
                log.info("User ${it.id} status successfully updated!")
                confirmationCodeRepository.delete(confirmationCode)

                return RegistrationResponseDto.success(it.id, it.registrationDate)
            } ?: log.error("Could not update user confirmation status!")
            return RegistrationResponseDto.error(RegistrationErrorReason.USER_NOT_FOUND)
        } ?: run {
            log.error("Could not find confirmation code $code")
            return RegistrationResponseDto.error(RegistrationErrorReason.INCORRECT_CONFIRMATION_CODE)
        }
    }

    /**
     * Метод создаёт код для подтверждения регистрации пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return пару - код и почту пользователя.
     */
    @Transactional
    fun createConfirmationCode(userId: String): Pair<String, String> {
        log.info("Creating confirmation code for user with id: $userId")

        userRepository.findUserById(UUID.fromString(userId))?.let { user ->
            val confirmationCode = ConfirmationCode()
            confirmationCode.user = user
            confirmationCode.code = generateCode()
            val savedCode = confirmationCodeRepository.save(confirmationCode)
            log.info("Confirmation code for user with id: $userId was saved! Code: ${savedCode.code}")

            return Pair(savedCode.code!!, user.credentials?.email!!)
        } ?: throw UserNotFoundException("User with id $userId doesn't exist!", null)
    }

    @Scheduled(
        fixedDelayString = "\${registration.cleanup.interval.minutes}",
        timeUnit = TimeUnit.MINUTES
    )
    fun cleanupConfirmationCodes() {
        confirmationCodeRepository.deleteExpiredConfirmationCodes()
    }

    fun sendCodeViaEmail(code: String, email: String?) {
        emailService.sendConfirmationCode(code, email)
    }

    private fun generateCode() : String = (CODE_MIN_VALUE..CODE_MAX_VALUE).random().toString()
}