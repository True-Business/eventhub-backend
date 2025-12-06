package ru.truebusiness.eventhub_backend.exceptions.storage

import java.util.UUID

class OwnerNotMatch private constructor(
    message: String,
) : ObjectStorageException(message) {
    companion object {
        fun byOwnerType(saved: String, requested: String): OwnerNotMatch {
            return OwnerNotMatch(
                "Saved ownerType $saved not match with requested: $requested"
            )
        }

        fun byOwnerId(saved: UUID, requested: UUID): OwnerNotMatch {
            return OwnerNotMatch(
                "Saved ownerId $saved request not match with requested: $requested"
            )
        }

        fun abError(): OwnerNotMatch {
            return OwnerNotMatch(
                "We know what the fuck is going on"
            )
        }
    }
}