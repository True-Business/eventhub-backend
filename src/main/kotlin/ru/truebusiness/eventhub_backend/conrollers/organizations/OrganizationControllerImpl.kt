package ru.truebusiness.eventhub_backend.conrollers.organizations

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.OrganizationDto
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
}