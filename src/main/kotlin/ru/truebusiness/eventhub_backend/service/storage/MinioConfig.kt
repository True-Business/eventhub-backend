package ru.truebusiness.eventhub_backend.service.storage

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig {
    @Bean
    fun minioClient(
        @Value("\${app.storage.user}")
        user: String,
        @Value("\${app.storage.password}")
        password: String,
        @Value("\${app.storage.hostPort}")
        connectionUrl: String,
    ): MinioClient {
        return MinioClient.builder()
            .endpoint(connectionUrl)
            .credentials(user, password)
            .build()
    }
}