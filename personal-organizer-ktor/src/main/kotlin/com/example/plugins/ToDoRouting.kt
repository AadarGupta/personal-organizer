package com.example.plugins

import com.example.models.http.ToDoCreationRequest
import com.example.models.http.ToDoEditCheckedRequest
import com.example.models.http.ToDoEditNameRequest
import com.example.models.http.ToDoListResponse
import com.example.services.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureToDoRoutes() {
    routing {
        post("/todo") {
            val toCreateOwner = call.request.queryParameters["user"] ?: ""
            if (toCreateOwner != "") {
                val toDoToBeCreated = call.receive<ToDoCreationRequest>()

                // call createToDo service
                val createdToDo =
                    createToDo(
                        toCreateOwner,
                        toDoToBeCreated.name,
                        toDoToBeCreated.isChecked
                    )
                call.respond(HttpStatusCode.OK, createdToDo)
                call.application.environment.log.info("ID: ${createdToDo.id}, User: ${toCreateOwner} - ToDo created.")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }

        put("/todo/name") {
            val toEditOwner = call.request.queryParameters["user"] ?: ""
            if (toEditOwner != "") {
                val toDoToEdit = call.receive<ToDoEditNameRequest>()

                // call editToDoName service
                editToDoName(
                    toEditOwner,
                    toDoToEdit.id,
                    toDoToEdit.name
                )
                call.respond(HttpStatusCode.OK, "ToDo name edited successfully.")
                call.application.environment.log.info("ID: ${toDoToEdit.id}, User: ${toEditOwner} - ToDo Name Changed.")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }

        put("/todo/checked") {
            val toEditOwner = call.request.queryParameters["user"] ?: ""
            if (toEditOwner != "") {
                val toDoToEdit = call.receive<ToDoEditCheckedRequest>()

                // call editToDoChecked service
                editToDoChecked(
                    toEditOwner,
                    toDoToEdit.id,
                    toDoToEdit.isChecked
                )
                call.respond(HttpStatusCode.OK, "ToDo isChecked edited successfully.")
                call.application.environment.log.info("ID: ${toDoToEdit.id}, User: ${toEditOwner} - ToDo isChecked Changed.")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }

        delete("/todo") {
            val toDeleteOwner = call.request.queryParameters["user"] ?: ""
            if (toDeleteOwner != "") {
                val toDeleteId = call.request.queryParameters["id"]?.toInt() ?: -1

                // call deleteToDo service
                deleteToDo(
                    toDeleteOwner,
                    toDeleteId
                )
                call.respond(HttpStatusCode.OK, "ToDo deleted successfully.")
                call.application.environment.log.info("ID: ${toDeleteId}, User: ${toDeleteOwner} - ToDo Deleted.")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }

        get("/todos") {
            val targetOwner = call.request.queryParameters["user"] ?: ""
            if (targetOwner != "") {
                // call getAllToDos service
                val toDoList : ToDoListResponse = getAllToDos(targetOwner);
                call.respond(toDoList)
                call.application.environment.log.info("User: ${targetOwner}, All todos requested.")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }
    }
}
