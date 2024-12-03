package ru.truebusiness

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class LivePoster

fun main(args: Array<String>) {
    SpringApplication.run(LivePoster::class.java, *args)
}