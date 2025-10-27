package ru.truebusiness.eventhub_backend.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.Organization

@Repository
interface OrganizationRepository: JpaRepository<Organization, UUID>, JpaSpecificationExecutor<Organization> {
    fun existsByName(name: String): Boolean
}