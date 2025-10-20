package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.exceptions.OrganizationAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.OrganizationNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.OrganizationMapper
import ru.truebusiness.eventhub_backend.repository.OrganizationRepository
import ru.truebusiness.eventhub_backend.repository.entity.Organization
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel
import ru.truebusiness.eventhub_backend.service.model.SearchOrganizationModel
import ru.truebusiness.eventhub_backend.service.model.UpdateOrganizationModel
import java.util.UUID

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val organizationMapper: OrganizationMapper,
) {
    private val log by logger()

    @Transactional
    fun create(organizationModel: OrganizationModel): OrganizationModel {
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
        return organizationMapper.organizationEntityToOrganizationModel(newOrganization)
    }

    @Transactional
    fun getByID(id: UUID): OrganizationModel {
        val organization = organizationRepository.findById(id)
            .orElseThrow { OrganizationNotFoundException("Organization with id '${id}' does not exist", null) }

        log.info("Organization found!")
        return organizationMapper.organizationEntityToOrganizationModel(organization)
    }

    @Transactional
    fun update(updateOrganizationModel: UpdateOrganizationModel): OrganizationModel {
        log.info("Updating organization: ${updateOrganizationModel.id}")

        updateOrganizationModel.name?.let {
            if (organizationRepository.existsByName(it)) {
                throw OrganizationAlreadyExistsException(
                    "Organization with name '${updateOrganizationModel.name}' already exists", null
                )
            }
        }

        val organization = organizationRepository.findById(updateOrganizationModel.id)
            .orElseThrow { OrganizationNotFoundException("Organization with id '${updateOrganizationModel.id}' does not exist", null) }
        organizationMapper.updateOrganizationModelToOrganizationEntity(updateOrganizationModel, organization)
        val updatedOrganization = organizationRepository.save(organization)

        log.info("Organization updated successfully!")
        return organizationMapper.organizationEntityToOrganizationModel(updatedOrganization)
    }

    @Transactional
    fun deleteById(id: UUID) {
        organizationRepository.findById(id).ifPresentOrElse(
            organizationRepository::delete
        ) {
            throw OrganizationNotFoundException("Organization with id '${id}' does not exist", null)
        }
        log.info("Organization deleted!")
    }

    fun search(searchModel: SearchOrganizationModel): List<OrganizationModel> {
        log.info("Searching for organization with \"${searchModel.search}\"...")
        // TODO: actual logic
        val found: List<OrganizationModel> = listOf()
        log.info("Found entries: ${found.count()}")
        return found
    }
}