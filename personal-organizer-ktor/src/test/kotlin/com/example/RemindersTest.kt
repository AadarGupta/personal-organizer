package com.example

import com.example.models.db.FileDbObject
import com.example.models.db.ReminderDbObject
import com.example.models.db.ToDoDbObject
import com.example.models.db.UserDbObject
import com.example.plugins.configureReminderRoutes
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
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
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals

class RemindersTest {

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
                    configureReminderRoutes()
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
    fun testReminderGetRequest() = testSuspend {
        val response = testApp.client.get("/reminders?user=test")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"items":[]}""", response.bodyAsText())
    }

    @Test
    fun testBadReminderRequests() = testSuspend {
        val createResponse = testApp.client.post("/reminder")

        assertEquals(HttpStatusCode.Unauthorized, createResponse.status)
        assertEquals("User not provided.", createResponse.bodyAsText())

        val deleteResponse = testApp.client.delete("/reminder")

        assertEquals(HttpStatusCode.Unauthorized, deleteResponse.status)
        assertEquals("User not provided.", deleteResponse.bodyAsText())

        val editResponse = testApp.client.put("/reminder")

        assertEquals(HttpStatusCode.Unauthorized, editResponse.status)
        assertEquals("User not provided.", editResponse.bodyAsText())
    }

    @Test
    fun testReminderCreateRequest() = testSuspend {
        val createRequestBody = JsonObject(
            mapOf(
                "name" to JsonPrimitive("testReminder"),
                "year" to JsonPrimitive("2023"),
                "month" to JsonPrimitive("12"),
                "day" to JsonPrimitive("31"),
                "time" to JsonPrimitive("1"),
                "isChecked" to JsonPrimitive("false")
            )
        )

        val response = testApp.client.post("/reminder?user=test") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(createRequestBody.toString())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"id":1,"owner":"test","itemName":"testReminder","year":"2023","month":"12","day":"31","time":"1","isChecked":false}""", response.bodyAsText())
    }

    @Test
    fun testReminderDeleteRequest() = testSuspend {
        val createRequestBody = JsonObject(
            mapOf(
                "name" to JsonPrimitive("testReminder"),
                "year" to JsonPrimitive("2023"),
                "month" to JsonPrimitive("12"),
                "day" to JsonPrimitive("31"),
                "time" to JsonPrimitive("1"),
                "isChecked" to JsonPrimitive("false")
            )
        )
        testApp.client.post("/reminder?user=test") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(createRequestBody.toString())
        }

        val response = testApp.client.delete("/reminder?user=test&id=1")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Reminder deleted successfully.", response.bodyAsText())

        val getResponse = testApp.client.get("/reminders?user=test")
        assertEquals("""{"items":[]}""", getResponse.bodyAsText())
    }

    @Test
    fun testReminderEditRequest() = testSuspend {
        val createRequestBody = JsonObject(
            mapOf(
                "name" to JsonPrimitive("testReminder"),
                "year" to JsonPrimitive("2023"),
                "month" to JsonPrimitive("12"),
                "day" to JsonPrimitive("31"),
                "time" to JsonPrimitive("1"),
                "isChecked" to JsonPrimitive("false")
            )
        )
        testApp.client.post("/reminder?user=test") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(createRequestBody.toString())
        }

        val editRequestBody = JsonObject(
            mapOf(
                "id" to JsonPrimitive(1),
                "name" to JsonPrimitive("newName"),
                "year" to JsonPrimitive("2024"),
                "month" to JsonPrimitive("1"),
                "day" to JsonPrimitive("1"),
                "time" to JsonPrimitive("1")
            )
        )

        val response = testApp.client.put("/reminder?user=test") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(editRequestBody.toString())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Reminder edited successfully.", response.bodyAsText())

        val getResponse = testApp.client.get("/reminders?user=test")
        assertEquals("""{"items":[{"id":1,"owner":"test","itemName":"newName","year":"2024","month":"1","day":"1","time":"1","isChecked":false}]}""", getResponse.bodyAsText())
    }

}