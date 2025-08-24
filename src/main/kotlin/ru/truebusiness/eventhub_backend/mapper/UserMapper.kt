package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.truebusiness.eventhub_backend.conrollers.dto.RegistrationDto
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.service.model.UserRegistrationModel

@Mapper(componentModel = "spring")
interface UserMapper {
    fun registrationDtoToUserRegistrationModel(registrationDto: RegistrationDto): UserRegistrationModel

    @Mapping(target = "username", source = "username")
    @Mapping(target = "shortId", source = "shortId")
    fun userModelToUserEntity(user: UserRegistrationModel): User
}