package com.example.plugins

import com.example.models.http.ReminderCreationRequest
import com.example.models.http.ReminderEditRequest
import com.example.models.http.ReminderListResponse
import com.example.services.createReminder
import com.example.services.deleteReminder
import com.example.services.editReminder
import com.example.services.getAllReminders
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureReminderRoutes() {
    routing {
        post("/reminder") {
            val toCreateOwner = call.request.queryParameters["user"] ?: ""
            if (toCreateOwner != "") {
                val reminderToBeCreated = call.receive<ReminderCreationRequest>()

                // call createReminder service
                val createdReminder =
                    createReminder(
                        toCreateOwner,
                        reminderToBeCreated.name,
                        reminderToBeCreated.year,
                        reminderToBeCreated.month,
                        reminderToBeCreated.day,
                        reminderToBeCreated.time,
                        reminderToBeCreated.isChecked
                    )
                call.respond(HttpStatusCode.OK, createdReminder)
                call.application.environment.log.info("ID: ${createdReminder.id}, User: ${toCreateOwner} - Reminder created.")
            }   else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }

        put("/reminder") {
            val toEditOwner = call.request.queryParameters["user"] ?: ""
            if (toEditOwner != "") {
                val reminderToEdit = call.receive<ReminderEditRequest>()

                // call editReminder service
                editReminder(
                    toEditOwner,
                    reminderToEdit.id,
                    reminderToEdit.name,
                    reminderToEdit.year,
                    reminderToEdit.month,
                    reminderToEdit.day,
                    reminderToEdit.time
                )
                call.respond(HttpStatusCode.OK, "Reminder edited successfully.")
                call.application.environment.log.info("ID: ${reminderToEdit.id}, User: ${toEditOwner} - Reminder Changed.")
            }   else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }

        delete("/reminder") {
            val toDeleteOwner = call.request.queryParameters["user"] ?: ""
            if (toDeleteOwner != "") {
                val toDeleteId = call.request.queryParameters["id"]?.toInt() ?: -1

                // call deleteReminder service
                deleteReminder(
                    toDeleteOwner,
                    toDeleteId
                )
                call.respond(HttpStatusCode.OK, "Reminder deleted successfully.")
                call.application.environment.log.info("ID: ${toDeleteId}, User: ${toDeleteOwner} - Reminder Deleted.")
            }   else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }

        get("/reminders") {
            val targetOwner = call.request.queryParameters["user"] ?: ""
            if (targetOwner != "") {
                // call getAllReminders service
                val reminderList : ReminderListResponse = getAllReminders(targetOwner);
                call.respond(HttpStatusCode.OK, reminderList)
                call.application.environment.log.info("User: ${targetOwner} - All reminders requested.")
            }   else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }
    }
}
