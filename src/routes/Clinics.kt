package com.checkme.test.routes

import com.checkme.test.dao.AddClinicCheckUpData
import com.checkme.test.dao.ClinicData
import com.checkme.test.services.Clinics
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.clinics() {
    route("/clinics") {
        get {
            call.respond(Clinics.all())
        }
        get("{id}") {
            val clinicId = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)

            Clinics.get(clinicId)?.let { call.respond(it) }
        }
        post {
            val clinicData = call.receive<ClinicData>()

            call.respond(HttpStatusCode.Created, Clinics.save(clinicData))
        }
        put("{id}") {
            val clinicId = call.parameters["id"]?.toInt() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val clinicData = call.receive<ClinicData>()

            Clinics.update(clinicId, clinicData)?.let { call.respond(it) }
        }
        delete("{id}") {
            val clinicId = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)

            Clinics.delete(clinicId)

            call.respond(HttpStatusCode.NoContent)
        }
        post("{id}/check-up") {
            val clinicId = call.parameters["id"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
            val addCheckUpData = call.receive<AddClinicCheckUpData>()

            Clinics.addCheckUp(clinicId, addCheckUpData)?.let { call.respond(it) }
        }
        delete("{clinicId}/check-up/{checkUpId}") {
            val clinicId = call.parameters["clinicId"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val checkUpId = call.parameters["checkUpId"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)

            Clinics.deleteCheckUp(clinicId, checkUpId)?.let { call.respond(it) }
        }
    }
}
