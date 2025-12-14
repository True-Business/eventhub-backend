package ru.truebusiness.eventhub_backend.service.storage

import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioClientConfig(
    private val props: MinioConfig,
) {

    @Bean
    fun minioClient(): MinioClient =
        MinioClient.builder()
            .endpoint(props.url.internal)
            .credentials(props.user, props.password)
            .build()
}