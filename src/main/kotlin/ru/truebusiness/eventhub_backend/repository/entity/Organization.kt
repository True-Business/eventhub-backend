package ru.truebusiness.eventhub_backend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "organizations")
class Organization(
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false)
    var description: String,

    var address: String?,

    var pictureUrl: String?,

    @Column(nullable = false)
    var isVerified: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    var creator: User,

    @ManyToMany
    @JoinTable(
        name = "organization_admin",
        joinColumns = [JoinColumn(name = "organization_id")],
        inverseJoinColumns = [JoinColumn(name = "admin_id")]
    )
    var admins: MutableList<User> = mutableListOf()
)