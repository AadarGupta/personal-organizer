package com.example

import com.example.models.db.FileDbObject
import com.example.models.db.ReminderDbObject
import com.example.models.db.ToDoDbObject
import com.example.models.db.UserDbObject
import com.example.plugins.configureAuthRoutes
import com.example.plugins.configureFileRoutes
import com.example.plugins.configureToDoRoutes
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class FileTest {

    companion object {
        lateinit var testApp: TestApplication

        @BeforeClass
        @JvmStatic
        fun setup() {
            testApp = TestApplication {
                install(ContentNegotiation) {
                    json()
                }
                application {
                    configureFileRoutes()
                }
                environment {
                    config = MapApplicationConfig(
                        "ktor.deployment.port" to "8080",
                    )
                }
            }
        }

        @AfterClass
        @JvmStatic
        fun  teardown() {
            Files.deleteIfExists(Paths.get("personal-organizer-test.db"))
            testApp.stop()
        }

    }

    @Before
    fun refreshDB() = testSuspend {
        Database.connect("jdbc:sqlite:personal-organizer-test.db")
        transaction {
            SchemaUtils.create (ToDoDbObject)
            SchemaUtils.create (ReminderDbObject)
            SchemaUtils.create (FileDbObject)
            SchemaUtils.create (UserDbObject)

            UserDbObject.insert {
                it[username] = "test"
                it[password] = "password"
            }
        }
    }

    @After
    fun  clearDB() {
        Files.deleteIfExists(Paths.get("personal-organizer-test.db"))
    }

    @Test
    fun testFileGetRequest() = testSuspend {
        val response = testApp.client.get("/files?user=test")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"items":[]}""", response.bodyAsText())
    }

    @Test
    fun testBadFileRequests() = testSuspend {
        val createResponse = testApp.client.post("/file")

        assertEquals(HttpStatusCode.Unauthorized, createResponse.status)
        assertEquals("User not provided.", createResponse.bodyAsText())

        val deleteResponse = testApp.client.delete("/file")

        assertEquals(HttpStatusCode.Unauthorized, deleteResponse.status)
        assertEquals("User not provided.", deleteResponse.bodyAsText())

        val editResponse = testApp.client.put("/file")

        assertEquals(HttpStatusCode.Unauthorized, editResponse.status)
        assertEquals("User not provided.", editResponse.bodyAsText())
    }

    @Test
    fun testFileCreateRequest() = testSuspend {
        val createRequestBody = JsonObject(
            mapOf(
                "isFolder" to JsonPrimitive(false),
                "parent" to JsonPrimitive(0),
                "fileName" to JsonPrimitive("testFile"),
                "fileContent" to JsonPrimitive("")
            )
        )

        val response = testApp.client.post("/file?user=test") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(createRequestBody.toString())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"id":1,"owner":"test","isFolder":false,"parent":0,"fileName":"testFile","fileContent":""}""", response.bodyAsText())
    }

    @Test
    fun testFileDeleteRequest() = testSuspend {
        val createRequestBody = JsonObject(
            mapOf(
                "isFolder" to JsonPrimitive(false),
                "parent" to JsonPrimitive(0),
                "fileName" to JsonPrimitive("testFile"),
                "fileContent" to JsonPrimitive("")
            )
        )
        testApp.client.post("/file?user=test") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(createRequestBody.toString())
        }

        val response = testApp.client.delete("/file?user=test&id=1")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("File deleted successfully.", response.bodyAsText())

        val getResponse = testApp.client.get("/files?user=test")
        assertEquals("""{"items":[]}""", getResponse.bodyAsText())
    }

    @Test
    fun testFileEditRequest() = testSuspend {
        val createRequestBody = JsonObject(
            mapOf(
                "isFolder" to JsonPrimitive(false),
                "parent" to JsonPrimitive(0),
                "fileName" to JsonPrimitive("testFile"),
                "fileContent" to JsonPrimitive("")
            )
        )
        testApp.client.post("/file?user=test") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(createRequestBody.toString())
        }

        val editRequestBody = JsonObject(
            mapOf(
                "id" to JsonPrimitive(1),
                "isFolder" to JsonPrimitive(true),
                "parent" to JsonPrimitive(1),
                "fileName" to JsonPrimitive("newName"),
                "fileContent" to JsonPrimitive("newContent")
            )
        )

        val response = testApp.client.put("/file?user=test") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(editRequestBody.toString())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("File edited successfully.", response.bodyAsText())

        val getResponse = testApp.client.get("/files?user=test")
        assertEquals("""{"items":[{"id":1,"owner":"test","isFolder":true,"parent":1,"fileName":"newName","fileContent":"newContent"}]}""", getResponse.bodyAsText())
    }

}