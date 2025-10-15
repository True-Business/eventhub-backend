package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.organizations.dto.OrganizationDto
import ru.truebusiness.eventhub_backend.exceptions.organization.OrganizationAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.organization.OrganizationNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.OrganizationMapper
import ru.truebusiness.eventhub_backend.repository.OrganizationRepository
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel
import java.util.UUID
import org.springframework.dao.DataIntegrityViolationException
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
            /*
            id пользователя можно получать через principal и тогда не нужно
            проверять на его наличие в userRepository, хотя как будто желательно
            в catch блоке все же сделать проверку не навернулся ли наш констрейт
            все же, ибо конкурентность
            */
            val user = userRepository.getReferenceById(
                organizationModel.creatorId
            )

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