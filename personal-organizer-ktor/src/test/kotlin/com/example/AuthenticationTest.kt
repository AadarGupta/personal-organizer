package com.example

import com.example.models.db.FileDbObject
import com.example.models.db.ReminderDbObject
import com.example.models.db.ToDoDbObject
import com.example.models.db.UserDbObject
import com.example.plugins.configureAuthRoutes
import com.example.plugins.configureToDoRoutes
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
import org.junit.*
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals

class AuthenticationTest {

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
                    configureAuthRoutes()
                    configureToDoRoutes()
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
    fun testUserCreation() = testSuspend {
        val jsonMap = JsonObject(
            mapOf(
                "username" to JsonPrimitive("test-create"),
                "password" to JsonPrimitive("password")
            )
        )

        val response = testApp.client.post("/user/signup") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(jsonMap.toString())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("User created.", response.bodyAsText())
    }

    @Test
    fun testExistingUserCreation() = testSuspend {
        val jsonMap = JsonObject(
            mapOf(
                "username" to JsonPrimitive("test"),
                "password" to JsonPrimitive("password")
            )
        )

        val response = testApp.client.post("/user/signup") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(jsonMap.toString())
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals("User already exists.", response.bodyAsText())
    }

    @Test
    fun testUserLogin() = testSuspend {
        val jsonMap = JsonObject(
            mapOf(
                "username" to JsonPrimitive("test"),
                "password" to JsonPrimitive("password")
            )
        )

        val response = testApp.client.post("/user/login") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(jsonMap.toString())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Login success.", response.bodyAsText())
    }

    @Test
    fun testBadUserLogin() = testSuspend {
        val jsonMap = JsonObject(
            mapOf(
                "username" to JsonPrimitive("test"),
                "password" to JsonPrimitive("wrongPassword")
            )
        )

        val response = testApp.client.post("/user/login") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(jsonMap.toString())
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals("Login failed.", response.bodyAsText())
    }
}