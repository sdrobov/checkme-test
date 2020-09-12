package com.checkme.test

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*

@Suppress("unused")
fun Application.main() {
    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/test") {
            call.respondText("Test")
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
