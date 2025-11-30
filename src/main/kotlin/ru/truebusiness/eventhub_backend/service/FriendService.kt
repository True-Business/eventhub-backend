package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestStatus
import ru.truebusiness.eventhub_backend.exceptions.friends.FriendRequestAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.friends.FriendRequestException
import ru.truebusiness.eventhub_backend.exceptions.friends.SelfFriendRequestException
import ru.truebusiness.eventhub_backend.exceptions.users.UserNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.FriendMapper
import ru.truebusiness.eventhub_backend.repository.FriendRepository
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.entity.FriendRequest
import ru.truebusiness.eventhub_backend.service.model.CreateFriendRequestModel
import ru.truebusiness.eventhub_backend.service.model.FriendRequestModel
import java.util.UUID

@Service
class FriendService(
    private val friendRepository: FriendRepository,
    private val userRepository: UserRepository,
    private val friendMapper: FriendMapper,
) {
    private val log by logger()

    @Transactional
    fun create(createFriendRequestModel: CreateFriendRequestModel): FriendRequestModel {

        if (createFriendRequestModel.sender == createFriendRequestModel.receiver) {
            log.error(
                "Failed to create friend request from {} to {}, same user",
                createFriendRequestModel.sender, createFriendRequestModel.receiver
            )
            throw SelfFriendRequestException.withId(createFriendRequestModel.sender);
        }

        val sender = userRepository.findById(createFriendRequestModel.sender)
            .orElseThrow { UserNotFoundException.withId(createFriendRequestModel.sender) }
        val receiver = userRepository.findById(createFriendRequestModel.receiver)
            .orElseThrow { UserNotFoundException.withId(createFriendRequestModel.receiver) }

        friendRepository.findBySenderAndReceiver(sender, receiver).forEach {
            if (it.status == FriendRequestStatus.PENDING) {
                log.error(
                    "Failed to create friend request from {} to {}, one is already pending",
                    createFriendRequestModel.sender, createFriendRequestModel.receiver
                )
                throw FriendRequestAlreadyExistsException.withSenderAndReceiver(
                    createFriendRequestModel.sender, createFriendRequestModel.receiver
                )
            }
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

    fun rejectFriendRequest(friendRequestId: UUID) {
        val friendRequest = friendRepository.findById(friendRequestId)
            .orElseThrow{FriendRequestException("friend request with id: $friendRequestId does not exists")}
        friendRequest.status = FriendRequestStatus.REJECTED;
        friendRepository.save(friendRequest)
    }
}