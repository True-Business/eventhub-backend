package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy
import ru.truebusiness.eventhub_backend.conrollers.dto.UpdateOrganizationRequestDto
import org.mapstruct.MappingConstants
import ru.truebusiness.eventhub_backend.conrollers.dto.organizations.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.organizations.OrganizationDto
import ru.truebusiness.eventhub_backend.repository.entity.Organization
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel
import ru.truebusiness.eventhub_backend.service.model.UpdateOrganizationModel
import java.util.UUID
import ru.truebusiness.eventhub_backend.repository.entity.User

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface OrganizationMapper {
    fun createOrganizationRequestDtoToOrganizationModel(organizationRequestDto: CreateOrganizationRequestDto): OrganizationModel
    fun updateOrganizationRequestDtoToUpdatedOrganizationModel(id: UUID, organizationRequestDto: UpdateOrganizationRequestDto): UpdateOrganizationModel

    @Mapping(target = "creator.id", source = "creatorId")
    fun organizationModelToOrganizationEntity(organizationModel: OrganizationModel): Organization
    @Mapping(target = "creatorId", source = "creator.id")
    fun organizationEntityToOrganizationModel(organization: Organization): OrganizationModel

    fun organizationModelToOrganizationDto(organizationModel: OrganizationModel): OrganizationDto

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun updateOrganizationModelToOrganizationEntity(updateOrganizationModel: UpdateOrganizationModel, @MappingTarget organization: Organization)
    fun organizationDtoToOrganizationModel(
        organizationRequestDto: CreateOrganizationRequestDto
    ): OrganizationModel

    @Mapping(target = "id", source = "organizationModel.id")
    fun organizationModelToOrganizationEntity(
        organizationModel: OrganizationModel, creator: User
    ): Organization

    @Mapping(target = "creatorId", source = "creator.id")
    fun organizationEntityToOrganizationDTO(entity: Organization): OrganizationDto
}