package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy
import ru.truebusiness.eventhub_backend.conrollers.dto.UpdateUserRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.UserDto
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.service.model.UpdateUserModel
import ru.truebusiness.eventhub_backend.service.model.UserModel
import java.util.UUID

@Mapper(componentModel = "spring")
interface UserMapper {

    fun userEntityToUserModel(user: User): UserModel

    fun userModelToUserDto(userModel: UserModel): UserDto

    fun updateUserRequestDtoToUpdateUserModel(
        id: UUID, updateUserRequestDto: UpdateUserRequestDto): UpdateUserModel

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun updateUserModelToUserEntity(updateUserModel: UpdateUserModel, @MappingTarget user: User)
}