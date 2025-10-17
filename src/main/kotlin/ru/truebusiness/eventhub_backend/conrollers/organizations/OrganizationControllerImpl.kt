package ru.truebusiness.eventhub_backend.conrollers.organizations

import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.UpdateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.organizations.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.organizations.OrganizationDto
import ru.truebusiness.eventhub_backend.mapper.OrganizationMapper
import ru.truebusiness.eventhub_backend.service.OrganizationService

@RestController
class OrganizationControllerImpl(
    private val organizationService: OrganizationService,
    private val organizationMapper: OrganizationMapper
) : OrganizationController {
    override fun create(
        createOrganizationRequestDto: CreateOrganizationRequestDto
    ): ResponseEntity<OrganizationDto> {
        val dto = organizationService.create(
            organizationMapper.organizationDtoToOrganizationModel(
                createOrganizationRequestDto
            )
        )

        return ResponseEntity(dto, HttpStatus.CREATED)
    }

    override fun get(organizationID: UUID): ResponseEntity<OrganizationDto> {
        val organization = organizationService.getByID(organizationID)
        return ResponseEntity.ok(organization)
    }

    override fun update(
        organizationID: UUID,
        updateOrganizationRequestDto: UpdateOrganizationRequestDto
    ): ResponseEntity<OrganizationDto> {
        val response = organizationMapper.organizationModelToOrganizationDto(
            organizationService.update(
                organizationMapper.updateOrganizationRequestDtoToUpdatedOrganizationModel(
                    organizationID,
                    updateOrganizationRequestDto
                )
            )
        )
        return ResponseEntity.ok(response)
    }

    override fun delete( organizationID: UUID): ResponseEntity<Void> {
        organizationService.deleteById(organizationID)
        return ResponseEntity.noContent().build()
    }
}