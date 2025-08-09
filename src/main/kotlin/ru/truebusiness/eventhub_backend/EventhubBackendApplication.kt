package ru.truebusiness.eventhub_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventhubBackendApplication

fun main(args: Array<String>) {
	runApplication<EventhubBackendApplication>(*args)
}
