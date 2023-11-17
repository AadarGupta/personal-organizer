package com.example.models.db

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object ReminderDbObject: IntIdTable() {
    var itemName = varchar("itemName", 50)
    var year = varchar("year", 50)
    var month = varchar("month", 50)
    var day = varchar("day", 50)
    var time = varchar("time", 50)
}


@Serializable
data class ReminderDbModel(
    val id: Int,
    val itemName: String,
    val year: String,
    val month: String,
    val day: String,
    val time: String,
)