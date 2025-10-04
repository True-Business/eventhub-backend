package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
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
import java.time.Duration
import java.time.Instant
import java.util.UUID

@Service
class RegistrationService(
    private val userRepository: UserRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val confirmationCodeRepository: ConfirmationCodeRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    @param:Value("\${app.registration.confirmationCodeExpirationMinutes}")
    private val confirmationCodeExpirationMinutes: Long,
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

            val newUser = userRepository.save(User(username = "", shortId = "",
                isConfirmed = false, credentials = null))

            log.info("New user registered! User id = ${newUser.id}")
            log.info("Saving new user credentials...")

            UserCredentials(
                email = email,
                password = passwordEncoder.encode(password),
                user = newUser
            ).also(userCredentialsRepository::save)


            log.info("Credentials for user ${newUser.id} saved!")
            log.info("User created successfully! Warning: user is not confirmed!")

            return RegistrationResponseDto.pending(newUser.id, newUser.registrationDate)
        } catch (_: DataIntegrityViolationException) {
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
        val savedCode = confirmationCodeRepository.findByCode(code) ?: run {
            return RegistrationResponseDto.error(
                RegistrationErrorReason.INCORRECT_CONFIRMATION_CODE
            )
        }

        return when {
            Instant.now().isBefore(savedCode.expiresAt) -> {
                log.debug("Confirmation code {} found", savedCode)
                confirmationCodeRepository.delete(savedCode)
                with(savedCode.user) {
                    isConfirmed = true
                    userRepository.save(this)
                    log.debug("User {} status successfully updated!", id)
                    RegistrationResponseDto.success(id, registrationDate)
                }
            }

            else -> {
                RegistrationResponseDto.error(RegistrationErrorReason.CONFIRMATION_CODE_EXPIRED)
            }
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
        log.debug("Creating confirmation code for user with id: $userId")
        val user = userRepository.findUserById(UUID.fromString(userId)) ?: run {
            throw UserNotFoundException(
                "User with id $userId doesn't exist!", null
            )
        }

        val savedCode = ConfirmationCode(
            code = generateConfirmationCode(),
            expiresAt = calculateConfirmationCodeExpirationTime(),
            user = user
        ).let(confirmationCodeRepository::save)

        log.debug("Confirmation code for user with id: $userId was saved! Code: ${savedCode.code}")
        return Pair(savedCode.code, user.credentials?.email!!)
    }

    fun sendCodeViaEmail(code: String, email: String?) {
        emailService.sendConfirmationCode(code, email)
    }

    private fun generateConfirmationCode(): String =
        (CODE_MIN_VALUE..CODE_MAX_VALUE).random().toString()

    private fun calculateConfirmationCodeExpirationTime() = Instant.now().plus(
        Duration.ofMinutes(confirmationCodeExpirationMinutes)
    )
}