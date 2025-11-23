package ru.truebusiness.eventhub_backend.service.storage

import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig {
    @Bean
    fun minioClient(
        user: String,
        password: String,
        connectionUrl: String,
    ): MinioClient {
        return MinioClient.builder()
            .endpoint(connectionUrl)
            .credentials(user, password)
            .build()
    }
}