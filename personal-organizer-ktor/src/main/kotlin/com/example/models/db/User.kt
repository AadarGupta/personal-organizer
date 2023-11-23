package com.example.models.db

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object UserDbObject: IntIdTable() {
    var username = varchar("username", 50)
    var password = varchar("password", 100)
}

@Serializable
data class UserDbModel(
    var username: String,
    var password: String
)