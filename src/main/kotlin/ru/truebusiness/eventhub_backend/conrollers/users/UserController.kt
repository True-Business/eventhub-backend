package ru.truebusiness.eventhub_backend.conrollers.users

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.UpdateUserRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.UserDto
import ru.truebusiness.eventhub_backend.mapper.UserMapper
import ru.truebusiness.eventhub_backend.service.users.UserService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper
) {
    @PutMapping("/{id}")
    fun updateById(
        @PathVariable("id") id: UUID,
        @RequestBody updateUserRequestDto: UpdateUserRequestDto
    ): ResponseEntity<UserDto> {
        val userModel = userService.update(
            userMapper.updateUserRequestDtoToUpdateUserModel(id, updateUserRequestDto))
        return ResponseEntity.ok(userMapper.userModelToUserDto(userModel))
    }
}