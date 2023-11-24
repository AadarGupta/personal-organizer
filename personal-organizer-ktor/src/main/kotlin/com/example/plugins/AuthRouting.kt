package com.example.plugins

import com.example.models.http.UserLoginRequest
import com.example.models.http.UserSignupRequest
import com.example.services.createUser
import com.example.services.loginUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureAuthRoutes() {
    routing {
        post("/user/signup") {
            val userToBeCreated = call.receive<UserSignupRequest>()
            val userCreationSuccess = createUser(
                userToBeCreated.username,
                userToBeCreated.password
            )
            if (userCreationSuccess) {
                call.application.environment.log.info("User created. Username: ${userToBeCreated.username}")
                call.respond(HttpStatusCode.OK, message = "User created.")
            } else {
                call.application.environment.log.info("User already exists. Username: ${userToBeCreated.username}")
                call.respond(HttpStatusCode.Unauthorized, message = "User already exists.")
            }
        }


        post("/user/login") {
            val userToBeLoggedIn = call.receive<UserLoginRequest>()
            val loginSuccess = loginUser(
                userToBeLoggedIn.username,
                userToBeLoggedIn.password
            )
            if (loginSuccess) {
                call.application.environment.log.info("User logged in. Username: ${userToBeLoggedIn.username}")
                call.respond(HttpStatusCode.OK, message = "Login success.")
            } else {
                call.application.environment.log.info("User login failed. Username: ${userToBeLoggedIn.username}")
                call.respond(HttpStatusCode.Unauthorized, message = "Login failed.")
            }
        }


    }
}