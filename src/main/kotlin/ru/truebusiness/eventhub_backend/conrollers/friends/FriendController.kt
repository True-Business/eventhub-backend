package ru.truebusiness.eventhub_backend.conrollers.friends

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.AcceptFriendRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.CreateFriendRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendshipDto
import java.util.UUID

@RequestMapping("/api/v1/friend")
interface FriendController {
    @PostMapping("/request/send")
    fun sendRequest(
        @RequestBody(
            description = "Данные для создания запроса на дружбу",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = CreateFriendRequestDto::class),
                examples = [ExampleObject(
                    name = "Корректный запрос",
                    value = """{
                            "sender": "e1800733-370c-4b23-8f01-910477248c95",
                            "receiver": "5d3ae284-5a4d-41f0-ae35-65b39af25b4e",
                        }""",
                )],
            )],
        )
        @org.springframework.web.bind.annotation.RequestBody
        createFriendRequestDto: CreateFriendRequestDto
    ): ResponseEntity<FriendRequestDto>

    @PostMapping("/request/accept")
    fun acceptRequest(
        @RequestBody(
            description = "Данные для принятия запроса на дружбу",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = AcceptFriendRequestDto::class),
                examples = [ExampleObject(
                    name = "Корректный запрос",
                    value = """{
                            "requestId": "e1800733-370c-4b23-8f01-910477248c95"
                        }""",
                )],
            )],
        )
        @org.springframework.web.bind.annotation.RequestBody
        acceptFriendRequestDto: AcceptFriendRequestDto
    ): ResponseEntity<FriendshipDto>

    @DeleteMapping("/{friendRequestId}")
    fun removeFriendship(@PathVariable friendRequestId: UUID): ResponseEntity<Void>

    @PostMapping("/request/{friendRequestId}/decline")
    fun rejectFriendRequest(@PathVariable friendRequestId: UUID): ResponseEntity<Void>

    @GetMapping("/request/outgoing/{userId}")
    fun getOutgoingRequests(@PathVariable userId: UUID): ResponseEntity<List<FriendRequestDto>>

    @GetMapping("/request/incoming/{userId}")
    fun getIncomingRequests(@PathVariable userId: UUID): ResponseEntity<List<FriendRequestDto>>
}