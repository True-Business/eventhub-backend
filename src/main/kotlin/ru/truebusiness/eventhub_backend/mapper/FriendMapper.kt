package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.AcceptFriendRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.CreateFriendRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendshipDto
import ru.truebusiness.eventhub_backend.repository.entity.FriendRequest
import ru.truebusiness.eventhub_backend.repository.entity.Friendship
import ru.truebusiness.eventhub_backend.service.model.AcceptFriendRequestModel
import ru.truebusiness.eventhub_backend.service.model.CreateFriendRequestModel
import ru.truebusiness.eventhub_backend.service.model.FriendRequestModel
import ru.truebusiness.eventhub_backend.service.model.FriendshipModel

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

    @Mapping(source = "user1Id", target = "user1")
    @Mapping(source = "user2Id", target = "user2")
    fun friendshipModelToFriendshipDto(
        friendshipModel: FriendshipModel
    ): FriendshipDto

    @Mapping(target = "user1Id", source = "user1.id")
    @Mapping(target = "user2Id", source = "user2.id")
    fun friendshipEntityToFriendshipModel(
        friendship: Friendship
    ): FriendshipModel

    fun acceptFriendRequestDtoToAcceptFriendRequestModel(
        acceptFriendRequestDto: AcceptFriendRequestDto
    ): AcceptFriendRequestModel
}