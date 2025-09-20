package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.truebusiness.eventhub_backend.repository.entity.Organization
import java.util.*

@Repository
interface OrganizationRepository: JpaRepository<Organization, UUID>