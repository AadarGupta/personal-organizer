package com.example.plugins

import com.example.models.http.PomodoroCreationRequest
import com.example.models.http.PomodoroEditCheckedRequest
import com.example.models.http.PomodoroListResponse
import com.example.models.http.ToDoEditCheckedRequest
import com.example.services.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configurePomodoroRoutes() {
    routing {
        post("/pomodoro") {
            val pomodoroToBeCreated = call.receive<PomodoroCreationRequest>()
            val createdPomodoro = createPomodoro(pomodoroToBeCreated.breaktime, pomodoroToBeCreated.worktime, pomodoroToBeCreated.isChecked)
            call.respond(createdPomodoro)
            call.application.environment.log.info("ID: ${createdPomodoro.id} - Pomodoro created.")
        }
        put("/pomodoro/checked") {
            val pomodoroToEdit = call.receive<PomodoroEditCheckedRequest>()
            editPomodoroChecked(pomodoroToEdit.id, pomodoroToEdit.isChecked)
            call.application.environment.log.info("ID: ${pomodoroToEdit.id} - Pomodoro isChecked Changed.")
        }


        get("/pomodoro") {
            val pomodoroList : PomodoroListResponse = getAllPomodoros();
            call.respond(pomodoroList)
            call.application.environment.log.info("All pomodoro requested.")
        }
    }
}
