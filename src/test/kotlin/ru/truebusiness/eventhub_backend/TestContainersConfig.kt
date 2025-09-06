package ru.truebusiness.eventhub_backend

import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer

//class TestContainersConfig: PostgreSQLContainer<TestContainersConfig>("docker.io/bitnami/postgresql:17.6.0-debian-12-r0")
class TestContainersConfig(image: String) : GenericContainer<TestContainersConfig>(image)

object Containers {
    val postgres = TestContainersConfig("docker.io/bitnami/postgresql:17.6.0-debian-12-r0").apply {
        addEnv("POSTGRESQL_DATABASE", "testdb")
        addEnv("POSTGRESQL_USERNAME", "test")
        addEnv("POSTGRESQL_PASSWORD", "test")
        withExposedPorts(5432)
        start()
    }


    fun jdbcUrl(): String =
        "jdbc:postgresql://${postgres.host}:${postgres.getMappedPort(5432)}/testdb"

    fun username(): String = "test"
    fun password(): String = "test"
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
            registry.add("spring.datasource.url", Containers::jdbcUrl)
            registry.add("spring.datasource.username", Containers::username)
            registry.add("spring.datasource.password", Containers::password)
        }
    }
}