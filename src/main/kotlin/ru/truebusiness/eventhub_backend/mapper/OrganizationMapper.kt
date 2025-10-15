package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import ru.truebusiness.eventhub_backend.conrollers.organizations.dto.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.organizations.dto.OrganizationDto
import ru.truebusiness.eventhub_backend.repository.entity.Organization
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface OrganizationMapper {
    fun organizationDtoToOrganizationModel(
        organizationRequestDto: CreateOrganizationRequestDto
    ): OrganizationModel

    @Mapping(target = "creator", ignore = true)
    fun organizationModelToOrganizationEntity(
        organizationModel: OrganizationModel, creator: User
    ): Organization

    @Mapping(target = "creatorId", source = "creator.id")
    fun organizationEntityToOrganizationDTO(entity: Organization): OrganizationDto
}