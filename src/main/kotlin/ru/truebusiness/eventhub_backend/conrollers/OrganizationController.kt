package ru.truebusiness.eventhub_backend.conrollers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.OrganizationDto
import ru.truebusiness.eventhub_backend.conrollers.dto.UpdateOrganizationRequestDto
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
        val response = organizationMapper.organizationModelToOrganizationDto(
            organizationService.create(
                organizationMapper.createOrganizationRequestDtoToOrganizationModel(createOrganizationRequestDto)
            )
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{organizationID}")
    fun get(@PathVariable("organizationID") organizationID: UUID): ResponseEntity<OrganizationDto> {
        val response = organizationMapper.organizationModelToOrganizationDto(
            organizationService.getByID(organizationID)
        )
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{organizationID}")
    fun update(
            @PathVariable("organizationID") organizationID: UUID,
            @RequestBody updateOrganizationRequestDto: UpdateOrganizationRequestDto
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

    @DeleteMapping("/{organizationID}")
    fun delete(@PathVariable("organizationID") organizationID: UUID): ResponseEntity<Void> {
        organizationService.deleteById(organizationID)
        return ResponseEntity.noContent().build()
    }
}