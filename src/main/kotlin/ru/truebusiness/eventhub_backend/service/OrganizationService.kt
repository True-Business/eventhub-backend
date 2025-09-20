package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.NewOrganizationResponse
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.OrganizationMapper
import ru.truebusiness.eventhub_backend.repository.OrganizationRepository
import ru.truebusiness.eventhub_backend.repository.entity.Organization
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val organizationMapper: OrganizationMapper,
) {
    private val log by logger()

    @Transactional
    fun createOrganization(organizationModel: OrganizationModel): NewOrganizationResponse {
        log.info("Creating new organization: ${organizationModel.name}")

        val organization: Organization = organizationMapper.organizationModelToOrganizationEntity(organizationModel)
        val newOrganization = organizationRepository.save(organization)

        log.info("New organization created successfully!")
        return NewOrganizationResponse(
            newOrganization.id,
            newOrganization.name,
            newOrganization.description,
            newOrganization.address,
            newOrganization.picture,
            newOrganization.creatorId
        )
    }
}