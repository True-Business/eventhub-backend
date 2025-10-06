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
abstract class OrganizationMapper {
    @Autowired
    protected lateinit var userRepository: UserRepository

    abstract fun organizationDtoToOrganizationModel(organizationRequestDto: CreateOrganizationRequestDto): OrganizationModel
    @Mapping(target = "creator", expression = "java(mapCreatorIdToUser(organizationModel.getCreatorId()))")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    abstract fun organizationModelToOrganizationEntity(organizationModel: OrganizationModel): Organization

    protected fun mapCreatorIdToUser(creatorId: UUID): User {
        userRepository.findUserById(creatorId)?.let {
            return it
        } ?: run {
            throw UserNotFoundException("Creator not found", null)
        }
    }
}