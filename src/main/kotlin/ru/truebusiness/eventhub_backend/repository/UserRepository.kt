package ru.truebusiness.eventhub_backend.repository

import java.time.Instant
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.ConfirmationCode
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.repository.entity.UserCredentials

@Repository
interface UserRepository: JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    fun findUserById(id: UUID): User?
    fun findUserByShortId(shortId: String): User?
    fun existsByShortId(id: String): Boolean
}

@Repository
interface UserCredentialsRepository : JpaRepository<UserCredentials, UUID> {
    fun findByEmail(email: String): UserCredentials?
    fun existsByEmail(email: String): Boolean
}

@Repository
interface ConfirmationCodeRepository : JpaRepository<ConfirmationCode, UUID> {
    fun findByCode(code: String): ConfirmationCode?
    fun deleteByExpiresAtBefore(expiresAt: Instant): Int
}