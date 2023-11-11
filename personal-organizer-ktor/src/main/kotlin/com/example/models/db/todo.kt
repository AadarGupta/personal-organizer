package com.example.models.db

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object ToDoDbObject: IntIdTable() {
    var itemName = varchar("itemName", 50)
    var isChecked = bool("isChecked")
}
@Serializable
data class ToDoDbModel(val id: Int, val itemName: String, val isChecked: Boolean)
