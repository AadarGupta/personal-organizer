package com.example

import com.example.models.db.FileDbObject
import com.example.models.db.ReminderDbObject
import com.example.models.db.ToDoDbObject
import com.example.models.db.UserDbObject
import com.example.plugins.configureAuthRoutes
import com.example.plugins.configureFileRoutes
import com.example.plugins.configureReminderRoutes
import com.example.plugins.configureToDoRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {

    Database.connect("jdbc:sqlite:personal-organizer.db")

    transaction {
        SchemaUtils.create (ToDoDbObject)
        SchemaUtils.create (ReminderDbObject)
        SchemaUtils.create (FileDbObject)
        SchemaUtils.create (UserDbObject)
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    configureToDoRoutes()
    configureReminderRoutes()
    configureFileRoutes()
    configureAuthRoutes()
}
