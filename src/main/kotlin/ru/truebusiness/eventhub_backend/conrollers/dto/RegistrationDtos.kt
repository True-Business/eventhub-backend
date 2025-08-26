package ru.truebusiness.eventhub_backend.conrollers.dto

import java.time.LocalDateTime
import java.util.*

data class UserInfoRegistrationDto(
    val id: UUID,
    val username: String,
    val shortId: String,
)

data class UserCredentialsRegistrationDto(
    val password: String,
    val email: String,
)

data class RegistrationResponseDto(
    val id: UUID?,
    val registrationDate: LocalDateTime?,
    override val status: String,
    override val reason: String?,
): BaseStatusDto(status, reason) {

    companion object {
        fun error(reason: RegistrationErrorReason): RegistrationResponseDto {
            return RegistrationResponseDto(
                id = null,
                registrationDate = null,
                status = RegistrationStatus.ERROR.name,
                reason = reason.name,
                )
        }

        fun pending(id: UUID?, registrationDate: LocalDateTime?): RegistrationResponseDto {
            return RegistrationResponseDto(
                id = id,
                registrationDate = registrationDate,
                status = RegistrationStatus.PENDING.name,
                reason = null,
            )
        }

        fun success(id: UUID?, registrationDate: LocalDateTime?): RegistrationResponseDto {
            return RegistrationResponseDto(
                id = id,
                registrationDate = registrationDate,
                status = RegistrationStatus.SUCCESS.name,
                reason = null
            )
        }
    }
}
