package com.checkme.test

import com.checkme.test.dao.*
import com.checkme.test.routes.checkUps
import com.checkme.test.routes.clinics
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>) {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    val dbHost = System.getenv("DB_HOST")
    val dbPort = System.getenv("DB_PORT")?.toInt() ?: 5432
    val dbName = System.getenv("DB_NAME")
    val dbUser = System.getenv("DB_USER")
    val dbPassword = System.getenv("DB_PASSWORD")

    Database.connect(
        "jdbc:postgresql://$dbHost:$dbPort/$dbName",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )

    transaction {
        SchemaUtils.create(Clinics, CheckUps, ClinicsCheckUps)
    }

    embeddedServer(
        Netty,
        port,
        watchPaths = listOf("ApplicationKt"),
        module = Application::module
    ).apply { start(wait = true) }
}

@Suppress("unused")
fun Application.module() {
    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        clinics()
        checkUps()
    }
}
