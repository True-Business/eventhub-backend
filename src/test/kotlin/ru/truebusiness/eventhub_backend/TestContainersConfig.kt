package ru.truebusiness.eventhub_backend

import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

class TestContainersConfig: PostgreSQLContainer<TestContainersConfig>("postgres:15")

object Containers {
    val postgres = TestContainersConfig().apply {
        withDatabaseName("testdb")
        withUsername("test")
        withPassword("test")
        start()
    }
}

abstract class BaseTest {
    companion object {
        @JvmStatic
        @BeforeAll
        fun startContainer() {
            Containers.postgres.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", Containers.postgres::getJdbcUrl)
            registry.add("spring.datasource.username", Containers.postgres::getUsername)
            registry.add("spring.datasource.password", Containers.postgres::getPassword)
        }
    }
}