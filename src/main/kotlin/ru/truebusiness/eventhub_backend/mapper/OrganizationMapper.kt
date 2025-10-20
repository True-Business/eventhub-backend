package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.OrganizationDto
import ru.truebusiness.eventhub_backend.conrollers.dto.SearchOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.UpdateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.repository.entity.Organization
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel
import ru.truebusiness.eventhub_backend.service.model.SearchOrganizationModel
import ru.truebusiness.eventhub_backend.service.model.UpdateOrganizationModel
import java.util.UUID

@Mapper(componentModel = "spring")
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

    fun organizationModelListToOrganizationDtoList(modelList: List<OrganizationModel>): List<OrganizationDto>
    fun searchOrganizationRequestDtoToSearchOrganizationModel(request: SearchOrganizationRequestDto): SearchOrganizationModel
}