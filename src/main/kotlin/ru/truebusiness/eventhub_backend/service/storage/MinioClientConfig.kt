package ru.truebusiness.eventhub_backend.service.storage

import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioClientConfig(
    private val props: MinioConfig,
) {

    @Bean
    fun minioInternalClient(): MinioClient = MinioClient.builder()
        .endpoint(props.url.internal)
        .credentials(props.user, props.password)
        .build()

    @Bean
    fun minioExternalClient(): MinioClient = MinioClient.builder()
        .endpoint(props.url.external)
        .credentials(props.user, props.password)
        .build()
}