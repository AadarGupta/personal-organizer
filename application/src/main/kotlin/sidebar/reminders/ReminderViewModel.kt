package sidebar.reminders;

import ReminderDataObject
import androidx.compose.runtime.mutableStateListOf
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class ReminderViewModel() {
    private var reminderList = mutableStateListOf<ReminderModel>()

    init {
        transaction {
            for (reminder in ReminderDataObject.selectAll()) {
                val reminderData = ReminderModel(reminder[ReminderDataObject.id], reminder[ReminderDataObject.itemName],  reminder[ReminderDataObject.year],  reminder[ReminderDataObject.month],  reminder[ReminderDataObject.day],  reminder[ReminderDataObject.time], reminder[ReminderDataObject.isChecked]);
                reminderList.add(reminderData)
            }
        }
    }

    fun getReminderList() : List<ReminderModel> {
        return reminderList;
    }

    fun isReminderEmpty() : Boolean {
        return reminderList.isEmpty()
    }

    fun addReminderList() : Int {
        transaction {
            var newItem = ReminderDataObject.insert {
                it[itemName] = "Reminder"
                it[year] = "2023"
                it[month] = "12"
                it[day] = "12"
                it[time] = "12:12:12"
                it[isChecked] = false
            } get ReminderDataObject.id

            reminderList.add(ReminderModel(newItem, "","2023" , "12", "12", "12:12:12",false))
        }
        return reminderList.size - 1
    }

    fun editReminderList(reminderItem: ReminderModel, inputName: String, inputYear: String, inputMonth: String, inputDay: String, inputTime: String) {
        val idx = reminderList.indexOf(reminderItem)
        reminderList[idx] = ReminderModel(reminderList[idx].id, inputName,inputYear,inputMonth,inputDay,inputTime, reminderList[idx].isChecked)
        transaction {
            ReminderDataObject.update({ ReminderDataObject.id eq reminderItem.id }) {
                it[itemName] = inputName 
                it[year] = inputYear
                it[month] = inputMonth
                it[day] = inputDay
                it[time] = inputTime
            }
        }
    }

    fun removeReminderItem(reminderItem: ReminderModel) {

        reminderList.remove(reminderItem)
        transaction {
            ReminderDataObject.deleteWhere { ReminderDataObject.id eq reminderItem.id }
        }
    }

    fun checkReminderItem(reminderItem: ReminderModel, value: Boolean) {
        val idx = reminderList.indexOf(reminderItem)
        reminderList[idx] = ReminderModel(reminderList[idx].id, reminderList[idx].itemName, reminderList[idx].year, reminderList[idx].month, reminderList[idx].day, reminderList[idx].time, value)

        transaction {
            ReminderDataObject.update({ ReminderDataObject.id eq reminderItem.id }) {
                it[isChecked] = value;
            }
        }
    }

    fun getItemByIdx(idx: Int): ReminderModel {
        return reminderList[idx]
    }

    fun getItemIdx(item: ReminderModel): Int {
        return reminderList.indexOf(item)
    }
}
