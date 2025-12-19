package ru.truebusiness.eventhub_backend.mapper

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectUpload
import ru.truebusiness.eventhub_backend.conrollers.dto.storage.ObjectsList
import ru.truebusiness.eventhub_backend.repository.storage.S3ObjectMetadata

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ObjectMetadataMapper {
    fun s3ObjectMetadata2UrlInfo(
        `object`: S3ObjectMetadata
    ): ObjectUpload.UrlInfo

    fun s3ObjectMetadata2ObjectListObject(
        `object`: S3ObjectMetadata,
    ): ObjectsList.Object

    fun s3ObjectMetadata2ObjectListObject(
        objects: List<S3ObjectMetadata>,
    ): List<ObjectsList.Object>
}