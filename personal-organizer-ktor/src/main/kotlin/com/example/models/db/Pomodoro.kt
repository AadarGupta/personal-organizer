package com.example.models.db

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object PomodoroDbObject: IntIdTable() {
    var breaktime = integer("breaktime")
    var worktime = integer("worktime")
    var isChecked = bool("isChecked")
}
@Serializable
data class PomodoroDbModel(val id: Int, val breaktime: Int, val worktime: Int,val isChecked: Boolean)
