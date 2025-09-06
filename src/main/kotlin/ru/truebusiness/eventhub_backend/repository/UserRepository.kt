package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.ConfirmationCode
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.repository.entity.UserCredentials
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, UUID> {
    fun findUserById(id: UUID): User?
    fun findUserByShortId(shortId: String): User?
}

@Repository
interface UserCredentialsRepository : JpaRepository<UserCredentials, UUID> {
    fun findByEmail(email: String): UserCredentials?
}

@Repository
interface ConfirmationCodeRepository : JpaRepository<ConfirmationCode, UUID> {
    fun findByCode(code: String): ConfirmationCode?
}