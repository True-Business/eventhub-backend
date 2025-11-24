package ru.truebusiness.eventhub_backend.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.Organization

@Repository
interface OrganizationRepository: JpaRepository<Organization, UUID>, JpaSpecificationExecutor<Organization> {
    fun existsByName(name: String): Boolean

    @Query("SELECT o FROM Organization o WHERE o.creator.id = :creatorId")
    fun findAllByCreatorId(@Param("creatorId") creatorId: UUID): List<Organization>
}