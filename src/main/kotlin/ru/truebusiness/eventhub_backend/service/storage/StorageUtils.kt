package ru.truebusiness.eventhub_backend.service.storage

import java.util.UUID
import ru.truebusiness.eventhub_backend.exceptions.storage.InvalidFileName
import ru.truebusiness.eventhub_backend.exceptions.storage.OwnerNotMatch
import ru.truebusiness.eventhub_backend.repository.storage.FileStatus
import ru.truebusiness.eventhub_backend.repository.storage.S3ObjectMetadata

object StorageUtils {
    val fileNamePattern: Regex = Regex(
        "^(?!.*\\.\\.)[\\p{L}0-9._\\- ]{1,255}$"
    )

    fun getFilePath(status: FileStatus, fileName: String): String {
        val prefix = status.prefix
        if (prefix.isBlank()) {
            return fileName
        }
        return "$prefix/$fileName"
    }

    private fun isValidFileNameStrict(fileName: String): Boolean {
        return fileName.matches(fileNamePattern)
    }

    fun validateOrigin(origin: List<String>) {
        origin.forEach(::validateOrigin)
    }

    fun validateOrigin(origin: String) {
        if (!isValidFileNameStrict(origin)) {
            throw InvalidFileName.byTargetPattern(fileNamePattern.pattern)
        }
    }


    fun validateMetasOwner(
        meta: List<S3ObjectMetadata>,
        ownerId: UUID,
        ownerType: String,
    ) {
        meta.find {
            it.ownerId != ownerId || it.ownerType != ownerType
        }?.let {
            throw when {
                it.ownerId != ownerId     -> {
                    OwnerNotMatch.byOwnerId(it.ownerId, ownerId)
                }

                it.ownerType != ownerType -> {
                    OwnerNotMatch.byOwnerType(it.ownerType, ownerType)
                }

                else                      -> {
                    OwnerNotMatch.abError()
                }
            }
        }
    }
}