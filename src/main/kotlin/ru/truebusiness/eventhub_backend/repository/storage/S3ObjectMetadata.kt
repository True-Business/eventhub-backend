package ru.truebusiness.eventhub_backend.repository.storage

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "object_meta")
class S3ObjectMetadata(
    @Id
    var id: UUID = UUID.randomUUID(),
    @Column(nullable = false)
    var origin: String,
    @Column(nullable = false)
    var `object`: String,
    @Column(nullable = false)
    var bucket: String,
    @Column(nullable = false)
    var ownerId: UUID,
    @Column(nullable = false)
    var ownerType: String,
    @Enumerated(EnumType.STRING)
    var status: FileStatus = FileStatus.PENDING,
    var expiry: Instant,
)
