package ru.truebusiness.eventhub_backend.conrollers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.NewOrganizationResponse
import ru.truebusiness.eventhub_backend.mapper.OrganizationMapper
import ru.truebusiness.eventhub_backend.service.OrganizationService
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel

@RestController
@RequestMapping("/api/v1/organization")
class OrganizationController(
    private val organizationService: OrganizationService,
    private val organizationMapper: OrganizationMapper
) {
    @PostMapping
    fun createOrganization(@RequestBody createOrganizationRequestDto: CreateOrganizationRequestDto): ResponseEntity<NewOrganizationResponse> {

        val organizationModel: OrganizationModel = organizationMapper.organizationDtoToOrganizationModel(createOrganizationRequestDto)

        // todo: creatorId вынимаем из хедера, чтобы юзер не мог в теле реквеста его подменять

        val response = organizationService.createOrganization(organizationModel)
        return ResponseEntity.ok(response)
    }
}