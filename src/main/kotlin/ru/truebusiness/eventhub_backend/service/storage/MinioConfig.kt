package ru.truebusiness.eventhub_backend.service.storage

import java.time.Duration
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.storage.minio")
data class MinioConfig(
    val user: String,
    val password: String,
    val url: MinioUrl,
    val bucket: Bucket,
) {
    data class MinioUrl(
        val internal: String,
        val external: String,
    )

    data class Bucket(
        val name: String,
        val nonConfirmedExpiry: Duration,
    )
}


