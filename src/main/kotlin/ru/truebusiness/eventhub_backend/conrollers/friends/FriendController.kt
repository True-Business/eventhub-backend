package ru.truebusiness.eventhub_backend.conrollers.friends

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.CreateFriendRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.friends.FriendRequestDto

@RequestMapping("/api/v1/friend")
interface FriendController {
    @PostMapping
    fun create(
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
}