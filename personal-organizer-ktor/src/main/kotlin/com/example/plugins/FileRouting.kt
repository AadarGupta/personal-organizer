package com.example.plugins

import com.example.models.http.FileCreationRequest
import com.example.models.http.FileEditRequest
import com.example.models.http.FileListResponse
import com.example.services.createFile
import com.example.services.deleteFile
import com.example.services.editFile
import com.example.services.getFiles
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureFileRoutes() {
    routing {
        post("/file") {
            val fileToBeCreated = call.receive<FileCreationRequest>()
            val createdFile =
                createFile(
                    fileToBeCreated.isFolder,
                    fileToBeCreated.parent,
                    fileToBeCreated.fileName,
                    fileToBeCreated.fileContent
                )
            call.respond(createdFile)
            call.application.environment.log.info("ID: ${createdFile.id} - File created.")
        }

        put("/file") {
            val fileToEdit = call.receive<FileEditRequest>()
            editFile(
                fileToEdit.id,
                fileToEdit.isFolder,
                fileToEdit.parent,
                fileToEdit.fileName,
                fileToEdit.fileContent
            )
            call.application.environment.log.info("ID: ${fileToEdit.id} - File Changed.")
        }

        delete("/file") {
            val toDeleteId = call.request.queryParameters["id"]?.toInt() ?: -1
            deleteFile(toDeleteId)
            call.application.environment.log.info("ID: ${toDeleteId} - File Deleted.")
        }

        get("/files") {
            val fileList : FileListResponse = getFiles();
            call.respond(fileList)
            call.application.environment.log.info("All files requested.")
        }
    }
}