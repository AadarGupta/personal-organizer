package com.example.plugins

import com.example.models.http.ToDoCreationRequest
import com.example.models.http.ToDoEditCheckedRequest
import com.example.models.http.ToDoEditNameRequest
import com.example.models.http.ToDoListResponse
import com.example.services.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/json/kotlinx-serialization") {
                call.respond(mapOf("hello" to "world"))
            }

        post("/todo") {
            val toDoToBeCreated = call.receive<ToDoCreationRequest>()
            val createdToDo = createToDo(toDoToBeCreated.name, toDoToBeCreated.isChecked)
            call.respond(createdToDo)
            call.application.environment.log.info("ID: ${createdToDo.id} - ToDo created.")
        }

        put("/todo/name") {
            val toDoToEdit = call.receive<ToDoEditNameRequest>()
            editToDoName(toDoToEdit.id, toDoToEdit.name)
            call.application.environment.log.info("ID: ${toDoToEdit.id} - ToDo Name Changed.")
        }

        put("/todo/checked") {
            val toDoToEdit = call.receive<ToDoEditCheckedRequest>()
            editToDoChecked(toDoToEdit.id, toDoToEdit.isChecked)
            call.application.environment.log.info("ID: ${toDoToEdit.id} - ToDo isChecked Changed.")
        }

        delete("/todo") {
            val toDeleteId = call.request.queryParameters["id"]?.toInt() ?: -1
            deleteToDo(toDeleteId)
            call.application.environment.log.info("ID: ${toDeleteId} - ToDo Deleted.")
        }



        get("/todos") {
            val toDoList : ToDoListResponse = getAllToDos();
            call.respond(toDoList)
            call.application.environment.log.info("All todos requested.")
        }
    }
}
