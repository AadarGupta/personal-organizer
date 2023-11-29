package com.example.models.db

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object ToDoDbObject: IntIdTable() {
    var owner = varchar("owner", 50)
    var itemName = varchar("itemName", 50)
    var isChecked = bool("isChecked")
}
@Serializable
data class ToDoDbModel(val id: Int, var owner: String, val itemName: String, val isChecked: Boolean)
