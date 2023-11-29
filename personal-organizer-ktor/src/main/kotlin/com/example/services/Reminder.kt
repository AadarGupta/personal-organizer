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
        ReminderDbObject.deleteWhere {ReminderDbObject.id.eq(id) and ReminderDbObject.owner.eq(itemOwner) }
    }
}


fun getAllReminders(itemOwner: String) : ReminderListResponse {
    val reminderList = mutableListOf<ReminderItem>()
    transaction {
        for (reminder in ReminderDbObject.selectAll()) {
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
                reminderList.add(reminderData)
            }
        }
    }
    val reminderListResponse = ReminderListResponse(reminderList)
    return reminderListResponse
}