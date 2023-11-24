package com.example.plugins

import com.example.models.http.FileCreationRequest
import com.example.models.http.FileEditRequest
import com.example.models.http.FileListResponse
import com.example.services.createFile
import com.example.services.deleteFile
import com.example.services.editFile
import com.example.services.getFiles
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureFileRoutes() {
    routing {
        post("/file") {
            val toCreateOwner = call.request.queryParameters["user"] ?: ""
            if (toCreateOwner != "") {
                val fileToBeCreated = call.receive<FileCreationRequest>()
                val createdFile =
                    createFile(
                        fileToBeCreated.isFolder,
                        toCreateOwner,
                        fileToBeCreated.parent,
                        fileToBeCreated.fileName,
                        fileToBeCreated.fileContent
                    )
                call.respond(createdFile)
                call.application.environment.log.info("ID: ${createdFile.id}, User: ${createdFile.owner} - File created.")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }

        }

        put("/file") {
            val toEditOwner = call.request.queryParameters["user"] ?: ""
            if (toEditOwner != "") {
                val fileToEdit = call.receive<FileEditRequest>()
                editFile(
                    fileToEdit.id,
                    toEditOwner,
                    fileToEdit.isFolder,
                    fileToEdit.parent,
                    fileToEdit.fileName,
                    fileToEdit.fileContent
                )
                call.respond(HttpStatusCode.OK, "File edited successfully.")
                call.application.environment.log.info("ID: ${fileToEdit.id}, User: ${toEditOwner} - File Changed.")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }

        delete("/file") {
            val toDeleteOwner = call.request.queryParameters["user"] ?: ""
            if (toDeleteOwner != "") {
                val toDeleteId = call.request.queryParameters["id"]?.toInt() ?: -1
                deleteFile(toDeleteOwner, toDeleteId)
                call.respond(HttpStatusCode.OK, "File deleted successfully.")
                call.application.environment.log.info("ID: ${toDeleteId}, User: ${toDeleteOwner} - File Deleted.")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }

        get("/files") {
            val targetOwner = call.request.queryParameters["user"] ?: ""
            if (targetOwner != "") {
                val fileList : FileListResponse = getFiles(targetOwner);
                call.respond(fileList)
                call.application.environment.log.info("User: ${targetOwner}, All files requested.")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "User not provided.")
            }
        }
    }
}