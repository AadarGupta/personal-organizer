package com.example.plugins

import com.example.models.http.ReminderCreationRequest
import com.example.models.http.ReminderEditCheckedRequest
import com.example.models.http.ReminderEditRequest
import com.example.models.http.ReminderListResponse
import com.example.services.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureReminderRoutes() {
    routing {
        post("/reminder") {
            val reminderToBeCreated = call.receive<ReminderCreationRequest>()
            val createdReminder =
                createReminder(
                    reminderToBeCreated.name,
                    reminderToBeCreated.year,
                    reminderToBeCreated.month,
                    reminderToBeCreated.day,
                    reminderToBeCreated.time,
                    reminderToBeCreated.isChecked)
            call.respond(createdReminder)
            call.application.environment.log.info("ID: ${createdReminder.id} - Reminder created.")
        }

        put("/reminder") {
            val reminderToEdit = call.receive<ReminderEditRequest>()
            editReminder(
                reminderToEdit.id,
                reminderToEdit.name,
                reminderToEdit.day,
                reminderToEdit.month,
                reminderToEdit.year,
                reminderToEdit.time
            )
            call.application.environment.log.info("ID: ${reminderToEdit.id} - Reminder Changed.")
        }

        put("/reminder/checked") {
            val reminderToEdit = call.receive<ReminderEditCheckedRequest>()
            editReminderChecked(reminderToEdit.id, reminderToEdit.isChecked)
            call.application.environment.log.info("ID: ${reminderToEdit.id} - Reminder isChecked Changed.")
        }

        delete("/reminder") {
            val toDeleteId = call.request.queryParameters["id"]?.toInt() ?: -1
            deleteReminder(toDeleteId)
            call.application.environment.log.info("ID: ${toDeleteId} - Reminder Deleted.")
        }

        get("/reminders") {
            val reminderList : ReminderListResponse = getAllReminders();
            call.respond(reminderList)
            call.application.environment.log.info("All reminders requested.")
        }
    }
}
