package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestStatus
import ru.truebusiness.eventhub_backend.exceptions.friends.FriendRequestAlreadyExistsException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.FriendMapper
import ru.truebusiness.eventhub_backend.repository.FriendRepository
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.entity.FriendRequest
import ru.truebusiness.eventhub_backend.service.model.CreateFriendRequestModel
import ru.truebusiness.eventhub_backend.service.model.FriendRequestModel

@Service
class FriendService (
    private val friendRepository: FriendRepository,
    private val userRepository: UserRepository,
    private val friendMapper: FriendMapper,
) {
    private val log by logger()

    @Transactional
    fun create(createFriendRequestModel: CreateFriendRequestModel): FriendRequestModel {
        val sender = userRepository.getReferenceById(createFriendRequestModel.sender)
        val receiver = userRepository.getReferenceById(createFriendRequestModel.receiver)

        if (friendRepository.existsBySenderAndReceiver(sender, receiver)) {
            log.error(
                "Failed to create friend request from {} to {}, already exists",
                createFriendRequestModel.sender, createFriendRequestModel.receiver
            )
            throw FriendRequestAlreadyExistsException.withSenderAndReceiver(
                createFriendRequestModel.sender, createFriendRequestModel.receiver
            )
        }

        val createdFriendRequest = friendRepository.save(
            FriendRequest(
                sender = sender,
                receiver = receiver,
                status = FriendRequestStatus.PENDING
            )
        )

        log.info("Create new friend request {}", createdFriendRequest.id)
        return friendMapper.friendRequestEntityToFriendRequestModel(
            createdFriendRequest
        )
    }
}