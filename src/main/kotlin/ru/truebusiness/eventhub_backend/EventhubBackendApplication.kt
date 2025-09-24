package ru.truebusiness.eventhub_backend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class EventhubBackendApplication

fun <T : Any> T.logger(): Lazy<Logger> = lazy {
	LoggerFactory.getLogger(this::class.java)
}

fun main(args: Array<String>) {
	runApplication<EventhubBackendApplication>(*args)
}
