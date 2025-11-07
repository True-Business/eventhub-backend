package ru.truebusiness.eventhub_backend.repository

import org.springframework.data.jpa.domain.Specification
import ru.truebusiness.eventhub_backend.repository.entity.Organization
import java.util.UUID

object OrganizationSpecs {

    fun withNameOrDescription(string: String?): Specification<Organization> =
        Specification { root, _, criteriaBuilder ->
            if (!string.isNullOrBlank()) {
                val searchPattern = "%${string.trim().lowercase()}%"
                criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern)
                )
            } else null
    }

    fun withCreatorShortId(shortId: String?): Specification<Organization> =
        Specification { root, _, criteriaBuilder ->
            if (!shortId.isNullOrBlank()) {
                val creatorJoin = root.join<Any, Any>("creator")
                criteriaBuilder.equal(creatorJoin.get<String>("shortId"), shortId)
            } else null
        }

    fun withAddress(address: String?): Specification<Organization> =
        Specification { root, _, criteriaBuilder ->
            if (!address.isNullOrBlank()) {
                val addressPattern = "%${address.trim().lowercase()}%"
                criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), addressPattern)
            } else null
        }

    fun isVerified(): Specification<Organization> =
        Specification { root, _, criteriaBuilder ->
            criteriaBuilder.isTrue(root.get("isVerified"))
        }

    fun isSubscribedBy(userId: UUID): Specification<Organization> =
        Specification { root, _, criteriaBuilder ->
            null
            //todo: finish method
        }

    fun isAdministratedBy(userId: UUID): Specification<Organization> =
        Specification { root, _, criteriaBuilder ->
            null
            //todo: finish method
        }
}