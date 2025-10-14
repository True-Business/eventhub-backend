package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.OrganizationDto
import ru.truebusiness.eventhub_backend.exceptions.organizations.OrganizationAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.organizations.OrganizationNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.OrganizationMapper
import ru.truebusiness.eventhub_backend.repository.OrganizationRepository
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel
import java.util.UUID
import org.springframework.dao.DataIntegrityViolationException
import ru.truebusiness.eventhub_backend.exceptions.users.UserNotFoundException
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
        try {
            val user = organizationModel.creatorId.let {
                if (!userRepository.existsById(it)) {
                    throw UserNotFoundException.withId(it)
                }
                userRepository.getReferenceById(it)
            }

            val createdOrganization =
                organizationMapper.organizationModelToOrganizationEntity(
                    organizationModel, user
                ).let(organizationRepository::save)

            log.info("Create new organization {}", createdOrganization.name)
            return organizationMapper.organizationEntityToOrganizationDTO(
                createdOrganization
            )
        } catch (e: DataIntegrityViolationException) {
            log.error(
                "Failed to create organization {}", organizationModel.name, e
            )
            throw OrganizationAlreadyExistsException.withName(
                organizationModel.name
            )
        }
    }

    @Transactional
    fun getByID(id: UUID): OrganizationDto {
        val organization = organizationRepository.findById(id).orElseThrow {
            OrganizationNotFoundException.withID(id)
        }

        log.info("Organization {} found", id)
        return organizationMapper.organizationEntityToOrganizationDTO(
            organization
        )
    }
}