package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy
import ru.truebusiness.eventhub_backend.conrollers.dto.FindUsersRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.UpdateUserRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.UserDto
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.service.model.UpdateUserModel
import ru.truebusiness.eventhub_backend.service.model.UserFiltersModel
import ru.truebusiness.eventhub_backend.service.model.UserModel
import java.util.UUID

@Mapper(componentModel = "spring")
interface UserMapper {

    @Mapping(source = "confirmed", target = "isConfirmed")
    fun userEntityToUserModel(user: User): UserModel

    fun userEntitiesToUserModels(user: List<User>): List<UserModel>

    @Mapping(source = "confirmed", target = "isConfirmed")
    fun userModelToUserDto(userModel: UserModel): UserDto

    fun userModelsToUserDtos(userModels: List<UserModel>): List<UserDto>

    fun updateUserRequestDtoToUpdateUserModel(
        id: UUID, updateUserRequestDto: UpdateUserRequestDto): UpdateUserModel

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun updateUserModelToUserEntity(updateUserModel: UpdateUserModel, @MappingTarget user: User)

    fun findUsersRequestDtoToUserFiltersModel(
        findUsersRequestDto: FindUsersRequestDto): UserFiltersModel
}
