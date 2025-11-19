package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.CreateFriendRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestDto
import ru.truebusiness.eventhub_backend.repository.entity.FriendRequest
import ru.truebusiness.eventhub_backend.service.model.CreateFriendRequestModel
import ru.truebusiness.eventhub_backend.service.model.FriendRequestModel

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface FriendMapper {
    fun createFriendRequestDtoToCreateFriendRequestModel(
        createFriendRequestDto: CreateFriendRequestDto
    ): CreateFriendRequestModel

    @Mapping(source = "senderId", target = "sender")
    @Mapping(source = "receiverId", target = "receiver")
    fun friendRequestModelToFriendRequestDto(
        friendRequestModel: FriendRequestModel
    ): FriendRequestDto

    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "receiverId", source = "receiver.id")
    fun friendRequestEntityToFriendRequestModel(
        friendRequest: FriendRequest
    ): FriendRequestModel
}