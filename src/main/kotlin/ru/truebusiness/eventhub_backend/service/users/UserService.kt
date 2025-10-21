package ru.truebusiness.eventhub_backend.service.users

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.truebusiness.eventhub_backend.exceptions.users.ShortIdException
import ru.truebusiness.eventhub_backend.exceptions.users.UserNotFoundException
import ru.truebusiness.eventhub_backend.logger
import ru.truebusiness.eventhub_backend.mapper.UserMapper
import ru.truebusiness.eventhub_backend.repository.UserRepository
import ru.truebusiness.eventhub_backend.repository.entity.User
import ru.truebusiness.eventhub_backend.service.model.UpdateUserModel
import ru.truebusiness.eventhub_backend.service.model.UserModel

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) {
    private val log by logger()

    @Transactional
    fun update(updateUserModel: UpdateUserModel): UserModel {
        log.info("Updating user: ${updateUserModel.id}")

        val user: User = userRepository.findById(updateUserModel.id)
            .orElseThrow { UserNotFoundException.withId(updateUserModel.id) }

        val userWithShortId: User? = userRepository.findUserByShortId(updateUserModel.shortId)
        if (userWithShortId != null && userWithShortId.id != user.id)
            throw ShortIdException.taken(updateUserModel.shortId)
        
        userMapper.updateUserModelToUserEntity(updateUserModel, user)

        val updatedUser = userRepository.save(user)
        log.info("User updated successfully!")
        return userMapper.userEntityToUserModel(updatedUser)
    }
}