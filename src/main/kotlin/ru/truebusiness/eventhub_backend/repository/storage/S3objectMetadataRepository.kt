package ru.truebusiness.eventhub_backend.repository.storage

import java.time.Instant
import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface S3objectMetadataRepository : JpaRepository<S3ObjectMetadata, UUID> {
    fun findAllByStatusIs(status: FileStatus): List<S3ObjectMetadata>
    fun findAllByExpiryBeforeAndStatus(
        expiry: Instant, status: FileStatus
    ): List<S3ObjectMetadata>

    fun findAllByOwnerIdAndOwnerType(
        ownerId: UUID, ownerType: String, pageable: Pageable
    ): List<S3ObjectMetadata>
}