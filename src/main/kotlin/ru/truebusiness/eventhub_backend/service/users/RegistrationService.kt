package ru.truebusiness.eventhub_backend.service.users

import jakarta.transaction.Transactional
import java.time.Duration
import java.time.Instant
import java.util.UUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.users.RegistrationResponseDto
import ru.truebusiness.eventhub_backend.exceptions.users.InvalidConfirmationCode
import ru.truebusiness.eventhub_backend.exceptions.users.CredentialsException
import ru.truebusiness.eventhub_backend.exceptions.users.UserAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.users.UserNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.UserMapper
import ru.truebusiness.eventhub_backend.repository.ConfirmationCodeRepository
import ru.truebusiness.eventhub_backend.repository.UserCredentialsRepository
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.entity.ConfirmationCode
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.repository.entity.UserCredentials
import ru.truebusiness.eventhub_backend.service.EmailService
import ru.truebusiness.eventhub_backend.service.model.UserModel

@Service
class RegistrationService(
    private val userRepository: UserRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val confirmationCodeRepository: ConfirmationCodeRepository,
    private val userMapper: UserMapper,
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
     * @throws UserAlreadyExistsException
     *
     * @return идентификатор созданного пользователя и дату регистрации.
     */
    @Transactional
    fun preRegisterUser(
        email: String, password: String
    ): RegistrationResponseDto {
        if (userCredentialsRepository.findByEmail(email) != null) {
            throw UserAlreadyExistsException.withEmail(email)
        }

        log.debug("Started registration of new user {}", email)
        val newUser = userRepository.save(User(username = "", shortId = "",
            bio = "", isConfirmed = false, credentials = null))

        log.debug("New user registered! User {}:{}", email, newUser.id)

        UserCredentials(
            email = email,
            password = passwordEncoder.encode(password),
            user = newUser
        ).also(userCredentialsRepository::save)


        log.debug("Credentials for user {}:{} saved!", email, newUser.id)
        log.debug(
            "User created successfully! Warning: user {} is not confirmed!",
            newUser.id
        )

        return RegistrationResponseDto.pending(
            newUser.id, newUser.registrationDate
        )
    }

    /**
     * Метод выполянет финальный этап регистрации: добавляет юзернейм и корткое имя пользователя.
     *
     * @param id идентификатор пользователя в приложении
     * @param username имя пользователя в приложении
     * @param shortId короткое имя пользователя в приложении
     * @throws UserAlreadyExistsException
     */
    @Transactional
    fun addUserInfo(
        id: UUID, username: String, shortId: String
    ): RegistrationResponseDto {
        if (userRepository.existsByShortId(shortId)) {
            throw UserAlreadyExistsException.withShortId(shortId)
        }

        val user = userRepository.findUserById(id) ?: run {
            log.error("User with id {} not found!", id)
            throw UserNotFoundException.withId(id)
        }

        user.let {
            it.username = username
            it.shortId = shortId
        }

        val updatedUser = userRepository.save(user)

        return RegistrationResponseDto.success(
            updatedUser.id, updatedUser.registrationDate
        )
    }

    /**
     * Метод првоеряет корректность введённого пользователем кода, который был отправлен ему на почту. После проверки
     * код удаляется.
     *
     * @param code введённый пользвоателем код.
     * @throws InvalidConfirmationCode
     */
    @Transactional
    fun verifyRegistrationCode(code: String): RegistrationResponseDto {
        val savedCode = confirmationCodeRepository.findByCode(code) ?: run {
            throw InvalidConfirmationCode.invalid(code)
        }
        if (Instant.now().isAfter(savedCode.expiresAt)) {
            throw InvalidConfirmationCode.expired(code)
        }

        log.debug("Confirmation code {} found", savedCode)
        confirmationCodeRepository.delete(savedCode)

        return with(savedCode.user) {
            isConfirmed = true
            userRepository.save(this)
            log.debug("User {} status successfully updated!", id)
            RegistrationResponseDto.success(id, registrationDate)
        }
    }

    /**
     * Метод создаёт код для подтверждения регистрации пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return пару - код и почту пользователя.
     * @throws UserNotFoundException
     */
    @Transactional
    fun createConfirmationCode(userId: UUID): Pair<String, String> {
        log.debug("Creating confirmation code for user with id: {}", userId)
        val user = userRepository.findUserById(userId) ?: run {
            throw UserNotFoundException.withId(userId)
        }

        val savedCode = ConfirmationCode(
            code = generateConfirmationCode(),
            expiresAt = calculateConfirmationCodeExpirationTime(),
            user = user
        ).let(confirmationCodeRepository::save)

        log.debug(
            "Confirmation code for user with id: {} was saved! Code: {}",
            userId,
            savedCode.code
        )
        return Pair(savedCode.code, user.credentials?.email!!)
    }

    fun sendCodeViaEmail(code: String, email: String) {
        emailService.sendConfirmationCode(code, email)
    }

    private fun generateConfirmationCode(): String =
        (CODE_MIN_VALUE..CODE_MAX_VALUE).random().toString()

    private fun calculateConfirmationCodeExpirationTime() = Instant.now().plus(
        Duration.ofMinutes(confirmationCodeExpirationMinutes)
    )

    fun forgotPassword(email: String) {
        val userCredentials = userCredentialsRepository.findByEmail(email)
        if (userCredentials == null) {
            throw UserNotFoundException.withEmail(email)
        }

        val res = createConfirmationCode(userCredentials.user.id)

        emailService.sendConfirmationCode(res.first, res.second)
    }
    
    @Transactional
    fun confirmForgotPassword(code: String, password: String) {
        val savedCode = confirmationCodeRepository.findByCode(code) ?: run {
            throw InvalidConfirmationCode.invalid(code)
        }
        if (Instant.now().isAfter(savedCode.expiresAt)) {
            throw InvalidConfirmationCode.expired(code)
        }

        log.debug("Confirmation code {} found", savedCode)
        confirmationCodeRepository.delete(savedCode)

        val userCredentials = savedCode.user.credentials
        if (userCredentials == null) {
            throw RuntimeException("failed to fetch user credentials")
        }

        userCredentials.password = passwordEncoder.encode(password)

        userCredentialsRepository.save<UserCredentials>(userCredentials)
    }

    fun login(email: String, password: String): UserModel {
        val userCredentials = userCredentialsRepository.findByEmail(email)
            ?: throw UserNotFoundException.withEmail(email)
        if (!passwordEncoder.matches(password, userCredentials.password)) {
            throw CredentialsException.invalid()
        }

        return userMapper.userEntityToUserModel(userCredentials.user)
    }
}