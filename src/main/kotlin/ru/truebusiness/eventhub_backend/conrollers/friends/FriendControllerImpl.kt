package ru.truebusiness.eventhub_backend.conrollers.friends

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.CreateFriendRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestDto
import ru.truebusiness.eventhub_backend.mapper.FriendMapper
import ru.truebusiness.eventhub_backend.service.FriendService
import java.util.UUID

@RestController
class FriendControllerImpl(
    private val friendService: FriendService,
    private val friendMapper: FriendMapper
) : FriendController {
    override fun create(
        createFriendRequestDto: CreateFriendRequestDto
    ): ResponseEntity<FriendRequestDto> {
        val model = friendService.create(
            friendMapper.createFriendRequestDtoToCreateFriendRequestModel(
                createFriendRequestDto
            )
        )

        return ResponseEntity(
            friendMapper.friendRequestModelToFriendRequestDto(model),
            HttpStatus.CREATED
        )
    }

    override fun removeFriendship(friendRequestId: UUID): ResponseEntity<Void> {
        friendService.removeFriendship(friendRequestId)
        return ResponseEntity(HttpStatus.OK)
    }
        
    override fun rejectFriendRequest(friendRequestId: UUID): ResponseEntity<Void> {
        friendService.rejectFriendRequest(friendRequestId)
        return ResponseEntity(HttpStatus.OK)
    }
}