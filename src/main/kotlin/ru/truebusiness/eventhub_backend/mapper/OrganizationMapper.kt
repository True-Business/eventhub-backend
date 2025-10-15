package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.beans.factory.annotation.Autowired
import ru.truebusiness.eventhub_backend.conrollers.dto.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.exceptions.UserNotFoundException
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.entity.Organization
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.service.model.OrganizationModel
import java.util.UUID

@Mapper(componentModel = "spring")
interface OrganizationMapper {
    fun organizationDtoToOrganizationModel(organizationRequestDto: CreateOrganizationRequestDto): OrganizationModel
    @Mapping(target = "creator.id", source = "creatorId")
    fun organizationModelToOrganizationEntity(organizationModel: OrganizationModel): Organization
}