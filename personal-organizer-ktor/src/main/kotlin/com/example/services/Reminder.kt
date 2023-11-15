package com.example.services

import com.example.models.db.ReminderDbModel
import com.example.models.db.ReminderDbObject
import com.example.models.http.ReminderItem
import com.example.models.http.ReminderListResponse
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


fun createReminder(
    name: String,
    newYear: String,
    newMonth: String,
    newDay: String,
    newTime: String,
) : ReminderDbModel {
    var targetReminderItem : ReminderDbModel =
        ReminderDbModel(
            -1,
            "",
            "",
            "",
            "",
            ""
        )

    transaction {
        val newReminder =
            ReminderDbObject.insert {
                it[itemName] = name
                it[year] = newYear
                it[month] = newMonth
                it[day] = newDay
                it[time] = newTime
            } get ReminderDbObject.id

        targetReminderItem =
            ReminderDbModel(
                newReminder.value,
                name,
                newYear,
                newMonth,
                newDay,
                newTime
            )
    }
    return targetReminderItem
}

fun editReminder(
    id: Int,
    newName: String,
    newYear: String,
    newMonth: String,
    newDay: String,
    newTime: String
) {
    transaction {
        ReminderDbObject.update({ReminderDbObject.id eq id}) {
            it[itemName] = newName
            it[year] = newYear
            it[month] = newMonth
            it[day] = newDay
            it[time] = newTime
        }
    }
}

fun deleteReminder(id: Int) {
    transaction {
        ReminderDbObject.deleteWhere { ReminderDbObject.id eq id }
    }
}


fun getAllReminders() : ReminderListResponse {
    val reminderList = mutableListOf<ReminderItem>()
    transaction {
        for (reminder in ReminderDbObject.selectAll()) {
            val reminderData =
                ReminderItem(
                    reminder[ReminderDbObject.id].value,
                    reminder[ReminderDbObject.itemName],
                    reminder[ReminderDbObject.year],
                    reminder[ReminderDbObject.month],
                    reminder[ReminderDbObject.day],
                    reminder[ReminderDbObject.time]
                )
            reminderList.add(reminderData)
        }
    }
    val reminderListResponse = ReminderListResponse(reminderList)
    return reminderListResponse
}