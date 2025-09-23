package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.Column
import java.util.UUID

@Entity
@Table(name = "confirmation_codes")
class ConfirmationCode {

    @Id
    var id: UUID = UUID.randomUUID()

    @Column(nullable = false)
    var code: String? = null

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null
}