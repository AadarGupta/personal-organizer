package com.example.services

import com.example.models.db.UserDbObject
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction


fun createUser(targetUsername: String, targetPassword: String) : Boolean {
    var userCreationSuccess = false
    transaction {
        val usernameExists = UserDbObject.select { UserDbObject.username eq targetUsername }
        if (usernameExists.empty()) {
            userCreationSuccess = true
            UserDbObject.insert {
                it[username] = targetUsername
                it[password] = targetPassword
            }
        }
    }
    return userCreationSuccess
}

fun loginUser(targetUsername: String, targetPassword: String) : Boolean {
    var loginSuccess = false
    transaction {
        val actualPassword = UserDbObject.select { UserDbObject.username eq targetUsername }.single()[UserDbObject.password]
        if (actualPassword == targetPassword) {
            loginSuccess = true
        }
    }
    return loginSuccess
}