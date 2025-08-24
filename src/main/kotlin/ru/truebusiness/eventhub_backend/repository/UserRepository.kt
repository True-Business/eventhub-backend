package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.repository.entity.UserCredentials
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<User, UUID>

@Repository
interface UserCredentialsRepository : JpaRepository<UserCredentials, UUID> {
    fun findByEmail(email: String): UserCredentials?
}