package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.OrganizationDto
import ru.truebusiness.eventhub_backend.exceptions.OrganizationAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.OrganizationNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.OrganizationMapper
import ru.truebusiness.eventhub_backend.repository.OrganizationRepository
import ru.truebusiness.eventhub_backend.repository.entity.Organization
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel
import java.util.UUID

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val organizationMapper: OrganizationMapper,
) {
    private val log by logger()

    @Transactional
    fun create(organizationModel: OrganizationModel): OrganizationDto {
        log.info("Creating new organization: ${organizationModel.name}")
        // todo: прокинуть сюда creatorId из хедера в контроллере как аргумент

        if (organizationRepository.existsByName(organizationModel.name)) {
            throw OrganizationAlreadyExistsException(
                "Organization with name '${organizationModel.name}' already exists", null
            )
        }

        val organization: Organization = organizationMapper.organizationModelToOrganizationEntity(organizationModel)
        val newOrganization = organizationRepository.save(organization)

        log.info("New organization created successfully!")
        return OrganizationDto(
            newOrganization.id,
            newOrganization.name,
            newOrganization.description,
            newOrganization.address,
            newOrganization.pictureUrl,
            newOrganization.creator.id
        )
    }

    @Transactional
    fun getByID(id: UUID): Organization {
        val organization = organizationRepository.findById(id)
            .orElseThrow { OrganizationNotFoundException("Organization with id '${id}' does not exist", null) }

        log.info("Organization found!")
        return organization
    }
}