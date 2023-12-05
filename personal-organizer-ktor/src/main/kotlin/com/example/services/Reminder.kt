package com.example.services

import com.example.models.db.ReminderDbModel
import com.example.models.db.ReminderDbObject
import com.example.models.http.ReminderItem
import com.example.models.http.ReminderListResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


fun createReminder(
    itemOwner: String,
    name: String,
    newYear: String,
    newMonth: String,
    newDay: String,
    newTime: String,
    checkedStatus: Boolean
) : ReminderDbModel {
    var targetReminderItem : ReminderDbModel =
        ReminderDbModel(
            -1,
            itemOwner,
            "",
            "",
            "",
            "",
            "",
            true
        )

    transaction {
        // create reminder
        val newReminder =
            ReminderDbObject.insert {
                it[owner] = itemOwner
                it[itemName] = name
                it[year] = newYear
                it[month] = newMonth
                it[day] = newDay
                it[time] = newTime
                it[isChecked] = checkedStatus
            } get ReminderDbObject.id

        targetReminderItem =
            ReminderDbModel(
                newReminder.value,
                itemOwner,
                name,
                newYear,
                newMonth,
                newDay,
                newTime,
                checkedStatus
            )
    }
    return targetReminderItem
}

fun editReminder(
    itemOwner: String,
    id: Int,
    newName: String,
    newYear: String,
    newMonth: String,
    newDay: String,
    newTime: String
) {
    transaction {
        // update reminder
        ReminderDbObject.update({ReminderDbObject.id.eq(id) and ReminderDbObject.owner.eq(itemOwner)}) {
            it[itemName] = newName
            it[year] = newYear
            it[month] = newMonth
            it[day] = newDay
            it[time] = newTime
        }
    }
}


fun deleteReminder(
    itemOwner: String,
    id: Int
) {
    transaction {
        // delete reminder
        ReminderDbObject.deleteWhere {ReminderDbObject.id.eq(id) and ReminderDbObject.owner.eq(itemOwner) }
    }
}


fun getAllReminders(itemOwner: String) : ReminderListResponse {
    val reminderList = mutableListOf<ReminderItem>()
    transaction {
        // get all reminders
        for (reminder in ReminderDbObject.selectAll()) {
            // if the reminder belongs to the user
            if (reminder[ReminderDbObject.owner] == itemOwner) {
                val reminderData =
                    ReminderItem(
                        reminder[ReminderDbObject.id].value,
                        reminder[ReminderDbObject.owner],
                        reminder[ReminderDbObject.itemName],
                        reminder[ReminderDbObject.year],
                        reminder[ReminderDbObject.month],
                        reminder[ReminderDbObject.day],
                        reminder[ReminderDbObject.time],
                        reminder[ReminderDbObject.isChecked]
                    )

                // add reminder to list
                reminderList.add(reminderData)
            }
        }
    }

    // create file list response
    val reminderListResponse = ReminderListResponse(reminderList)
    return reminderListResponse
}