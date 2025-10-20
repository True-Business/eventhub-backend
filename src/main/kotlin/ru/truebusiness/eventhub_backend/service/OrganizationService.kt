package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.organizations.OrganizationDto
import ru.truebusiness.eventhub_backend.exceptions.organization.OrganizationAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.organization.OrganizationNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.OrganizationMapper
import ru.truebusiness.eventhub_backend.repository.OrganizationRepository
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel
import ru.truebusiness.eventhub_backend.service.model.UpdateOrganizationModel
import java.util.UUID
import ru.truebusiness.eventhub_backend.repository.UserRepository

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository,
    private val organizationMapper: OrganizationMapper,
) {
    private val log by logger()

    @Transactional
    fun create(organizationModel: OrganizationModel): OrganizationDto {
        /*
        id пользователя можно получать через principal и тогда не нужно
        проверять на его наличие в userRepository
        */
        if (organizationRepository.existsByName(organizationModel.name)) {
            log.error(
                "Failed to create organization {}, organization already exists",
                organizationModel.name
            )
            throw OrganizationAlreadyExistsException.withName(organizationModel.name)
        }
        val user = userRepository.getReferenceById(organizationModel.creatorId)
        val createdOrganization =
            organizationMapper.organizationModelToOrganizationEntity(
                organizationModel, user
            ).let(organizationRepository::save)

        log.info("Create new organization {}", createdOrganization.name)
        return organizationMapper.organizationEntityToOrganizationDTO(
            createdOrganization
        )
    }

    fun getByID(id: UUID): OrganizationDto {
        val organization = organizationRepository.findById(id).orElseThrow {
            OrganizationNotFoundException.withID(id)
        }

        log.info("Organization {} found", id)
        return organizationMapper.organizationEntityToOrganizationDTO(
            organization
        )
    }

    @Transactional
    fun update(updateOrganizationModel: UpdateOrganizationModel): OrganizationModel {
        log.info("Updating organization: {}", updateOrganizationModel.id)

        updateOrganizationModel.name?.let {
            if (organizationRepository.existsByName(it)) {
                throw OrganizationAlreadyExistsException.withName(it                )
            }
        }

        val organization =
            organizationRepository.findById(updateOrganizationModel.id)
                .orElseThrow {
                    OrganizationNotFoundException.withID(updateOrganizationModel.id)
                }
        organizationMapper.updateOrganizationModelToOrganizationEntity(updateOrganizationModel, organization)
        val updatedOrganization = organizationRepository.save(organization)

        log.info("Organization {} updated", organization.id)
        return organizationMapper.organizationEntityToOrganizationModel(updatedOrganization)
    }

    @Transactional
    fun deleteById(id: UUID) {
        organizationRepository.findById(id).ifPresentOrElse(
            organizationRepository::delete
        ) {
            throw OrganizationNotFoundException.withID(id)
        }
        log.info("Organization {} deleted", id)
    }
}