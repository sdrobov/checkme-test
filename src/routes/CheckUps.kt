package com.checkme.test.routes

import com.checkme.test.dao.CheckUpData
import com.checkme.test.services.CheckUps
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.checkUps() {
    route("/check-ups") {
        get {
            call.respond(CheckUps.all())
        }
        get("{id}") {
            val checkUpId = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)

            CheckUps.get(checkUpId)?.let { call.respond(it) }
        }
        post {
            val checkUpData = call.receive<CheckUpData>()

            call.respond(CheckUps.save(checkUpData))
        }
        put("{id}") {
            val checkUpId = call.parameters["id"]?.toInt() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val checkUpData = call.receive<CheckUpData>()

            CheckUps.update(checkUpId, checkUpData)?.let { call.respond(it) }
        }
        delete("{id}") {
            val checkUpId = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)

            CheckUps.delete(checkUpId)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
