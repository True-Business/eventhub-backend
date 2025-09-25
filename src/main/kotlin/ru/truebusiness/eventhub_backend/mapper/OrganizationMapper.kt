package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.repository.entity.Organization
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel

@Mapper(componentModel = "spring")
interface OrganizationMapper {
    fun organizationDtoToOrganizationModel(organizationRequestDto: CreateOrganizationRequestDto): OrganizationModel
    @Mapping(target = "creator", ignore = true)
    fun organizationModelToOrganizationEntity(organizationModel: OrganizationModel): Organization
}