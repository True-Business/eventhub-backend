package ru.truebusiness.eventhub_backend.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestStatus
import ru.truebusiness.eventhub_backend.exceptions.friends.FriendRequestAlreadyExistsException
import ru.truebusiness.eventhub_backend.exceptions.friends.FriendRequestNotFoundException
import ru.truebusiness.eventhub_backend.exceptions.friends.FriendRequestWrongStatusException
import ru.truebusiness.eventhub_backend.exceptions.friends.FriendRequestException
import ru.truebusiness.eventhub_backend.exceptions.friends.SelfFriendRequestException
import ru.truebusiness.eventhub_backend.exceptions.users.UserNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.FriendMapper
import ru.truebusiness.eventhub_backend.repository.FriendRequestRepository
import ru.truebusiness.eventhub_backend.repository.FriendshipRepository
import ru.truebusiness.eventhub_backend.repository.FriendRequestSpecs
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.entity.FriendRequest
import ru.truebusiness.eventhub_backend.repository.entity.Friendship
import ru.truebusiness.eventhub_backend.service.model.AcceptFriendRequestModel
import ru.truebusiness.eventhub_backend.service.model.CreateFriendRequestModel
import ru.truebusiness.eventhub_backend.service.model.FriendRequestModel
import ru.truebusiness.eventhub_backend.service.model.FriendshipModel
import java.util.UUID

@Service
class FriendService (
    private val friendRequestRepository: FriendRequestRepository,
    private val friendshipRepository: FriendshipRepository,
    private val userRepository: UserRepository,
    private val friendMapper: FriendMapper,
) {
    private val log by logger()

    @Transactional
    fun createFriendRequest(createFriendRequestModel: CreateFriendRequestModel): FriendRequestModel {

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

        val allRequestsBetweenSpecifiedUsers = friendRequestRepository
            .findBySenderAndReceiver(sender, receiver) + friendRequestRepository
                .findBySenderAndReceiver(receiver, sender)
        allRequestsBetweenSpecifiedUsers.forEach {
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

        val createdFriendRequest = friendRequestRepository.save(
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

    @Transactional
    fun acceptFriendRequest(
        acceptFriendRequestModel: AcceptFriendRequestModel
    ): FriendshipModel {
        val request = friendRequestRepository.findById(acceptFriendRequestModel.requestId)
            .orElseThrow { FriendRequestNotFoundException.withId(acceptFriendRequestModel.requestId) }
        if (request.status != FriendRequestStatus.PENDING) {
            log.error("Trying to accept request ${request.id} which already has ${request.status.name} status.")
            throw FriendRequestWrongStatusException.withStatus(request.status)
        }

        val createdFriendship = friendshipRepository.save(
            Friendship(
                user1 = request.sender,
                user2 = request.receiver
            )
        )

        request.status = FriendRequestStatus.ACCEPTED
        request.acceptedAt = createdFriendship.since
        friendRequestRepository.save(request)

        return friendMapper.friendshipEntityToFriendshipModel(
            createdFriendship
        )
    }

    fun removeFriendship(friendRequestId: UUID) {
        friendRequestRepository.deleteById(friendRequestId)
    }

    fun rejectFriendRequest(friendRequestId: UUID) {
        val friendRequest = friendRequestRepository.findById(friendRequestId)
            .orElseThrow { FriendRequestException("friend request with id: $friendRequestId does not exists") }
        friendRequest.status = FriendRequestStatus.REJECTED;
        friendRequestRepository.save(friendRequest)
    }

    @Transactional
    fun getOutgoingRequests(userId: UUID): List<FriendRequestModel> {
        val sender = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException.withId(userId) }

        val specification = FriendRequestSpecs
            .withSender(sender)
            .and(FriendRequestSpecs.withStatus(FriendRequestStatus.ACCEPTED, invert = true))

        val requests = friendRequestRepository.findAll(specification)
        return friendMapper.friendRequestEntityListToFriendRequestModelList(
            requests
        )
    }

    @Transactional
    fun getIncomingRequests(userId: UUID): List<FriendRequestModel> {
        val receiver = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException.withId(userId) }

        val specification = FriendRequestSpecs
            .withReceiver(receiver)
            .and(FriendRequestSpecs.withStatus(FriendRequestStatus.PENDING))

        val requests = friendRequestRepository.findAll(specification)
        return friendMapper.friendRequestEntityListToFriendRequestModelList(
            requests
        )
    }
}