package ru.truebusiness.eventhub_backend.conrollers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.exceptions.OrganizationAlreadyExistsException
import ru.truebusiness.eventhub_backend.mapper.OrganizationMapper
import ru.truebusiness.eventhub_backend.service.OrganizationService

@RestController
@RequestMapping("/api/v1/organization")
class OrganizationController(
    private val organizationService: OrganizationService,
    private val organizationMapper: OrganizationMapper
) {
    @PostMapping
    fun createOrganization(@RequestBody createOrganizationRequestDto: CreateOrganizationRequestDto): ResponseEntity<Any> {
        return try {
            val response = organizationService.createOrganization(organizationMapper.organizationDtoToOrganizationModel(createOrganizationRequestDto))
            ResponseEntity.ok(response)
        } catch (e: OrganizationAlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(
                mapOf(
                    "error" to "Organization already exists",
                    "message" to e.message
                )
            )
        }
    }
}