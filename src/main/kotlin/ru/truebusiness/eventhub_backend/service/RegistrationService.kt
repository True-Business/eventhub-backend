package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
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
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class RegistrationService(
    private val userRepository: UserRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val confirmationCodeRepository: ConfirmationCodeRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    @Value("\${app.registration.token-expiration-minutes}")
    private val tokenExpirationMinutes: Long,
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
        val confCode = confirmationCodeRepository.findByCode(code)
        return if (confCode != null && Instant.now().isBefore(confCode.expiresAt)) {
            log.debug("Confirmation code {} found", confCode)
            confirmationCodeRepository.delete(confCode)
            confCode.user.let {
                it.isConfirmed = true
                userRepository.save(it)
                log.debug("User {} status successfully updated!", it.id)
                RegistrationResponseDto.success(it.id, it.registrationDate)
            }
        } else if (confCode != null) {
            RegistrationResponseDto.error(RegistrationErrorReason.CONFIRMATION_CODE_EXPIRED)
        } else {
            log.error("Could not find confirmation code $code")
            RegistrationResponseDto.error(RegistrationErrorReason.INCORRECT_CONFIRMATION_CODE)
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
        val usr = userRepository.findUserById(UUID.fromString(userId))
        return if (usr != null) {
            val savedCode = ConfirmationCode(
                code =  generateCode(),
                expiresAt = getCodeExpiration(),
                user = usr
            ).let(confirmationCodeRepository::save)
            log.debug("Confirmation code for user with id: $userId was saved! Code: ${savedCode.code}")
            Pair(savedCode.code, usr.credentials?.email!!)
        } else {
            throw UserNotFoundException("User with id $userId doesn't exist!", null)
        }
    }

    @Scheduled(
        fixedDelayString = "\${app.registration.cleanup-interval-minutes}",
        timeUnit = TimeUnit.MINUTES
    )
    fun cleanupConfirmationCodes() {
        confirmationCodeRepository.deleteByExpiresAtBefore(Instant.now())
    }

    fun sendCodeViaEmail(code: String, email: String?) {
        emailService.sendConfirmationCode(code, email)
    }

    private fun generateCode() : String = (CODE_MIN_VALUE..CODE_MAX_VALUE).random().toString()
    private fun getCodeExpiration() =
        Instant.now().plus(Duration.ofMinutes(tokenExpirationMinutes))
}