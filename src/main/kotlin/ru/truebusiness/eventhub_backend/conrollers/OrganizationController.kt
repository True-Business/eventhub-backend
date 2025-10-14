package ru.truebusiness.eventhub_backend.conrollers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.OrganizationDto
import ru.truebusiness.eventhub_backend.mapper.OrganizationMapper
import ru.truebusiness.eventhub_backend.service.OrganizationService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/organization")
class OrganizationController(
    private val organizationService: OrganizationService,
    private val organizationMapper: OrganizationMapper
) {
    @PostMapping
    fun create(@RequestBody createOrganizationRequestDto: CreateOrganizationRequestDto): ResponseEntity<OrganizationDto> {
        val response = organizationService.create(organizationMapper.organizationDtoToOrganizationModel(createOrganizationRequestDto))
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{organizationID}")
    fun get(@PathVariable("organizationID") organizationID: UUID): ResponseEntity<OrganizationDto> {
        val organization = organizationService.getByID(organizationID)
        val response = OrganizationDto(
            organization.id,
            organization.name,
            organization.description,
            organization.address,
            organization.pictureUrl,
            organization.creator.id
        )
        return ResponseEntity.ok(response)
    }
}