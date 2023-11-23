package sidebar.reminders;

import MyHttp
import androidx.compose.runtime.mutableStateListOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class ReminderList(val items: List<ReminderModel>)

class ReminderViewModel() {
    private var reminderList = mutableStateListOf<ReminderModel>()

    init {
        val http = MyHttp()
        val getAllReminderResponse : String = http.get("reminders")
        println(getAllReminderResponse)
        val obj = Json.decodeFromString<ReminderList>(getAllReminderResponse)

        for (reminder in obj.items) {
            reminderList.add(reminder)
        }
    }

    fun getReminderList() : List<ReminderModel> {
        return reminderList;
    }

    fun getTodayReminderList() : List<ReminderModel> {
        var today = java.time.LocalDate.now()
        var currDay = today.dayOfMonth.toString()
        var currMonth = today.monthValue.toString()
        var currYear = today.year.toString()
        var currHour = if (java.time.LocalTime.now().hour > 12) (java.time.LocalTime.now().hour - 12).toString() else java.time.LocalTime.now().hour.toString()
        var currMinute = java.time.LocalTime.now().minute.toString()
        var currAMPM = if (java.time.LocalTime.now().hour < 12) "AM" else "PM"
        return reminderList.filter {
            it.year == currYear &&
            it.month == currMonth &&
            it.day == currDay //&&
            //it.time == "$currHour:$currMinute $currAMPM"
        };
    }

    fun isReminderEmpty() : Boolean {
        return reminderList.isEmpty()
    }

    fun addReminderList(
        itemName: String,
        year: String,
        month: String,
        day: String,
        time: String
    ) : Int {
        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "name" to JsonPrimitive(itemName),
                "year" to JsonPrimitive(year),
                "month" to JsonPrimitive(month),
                "day" to JsonPrimitive(day),
                "time" to JsonPrimitive(time),
                "isChecked" to JsonPrimitive(false)
            )
        )

        val createReminderResponse = http.post("reminder", body)
        val newItem = Json.decodeFromString<ReminderModel>(createReminderResponse.body())
        reminderList.add(newItem)

        return reminderList.size - 1
    }

    fun editReminderList(
        targetItem: ReminderModel,
        inputName: String,
        inputYear: String,
        inputMonth: String,
        inputDay: String,
        inputTime: String
    ) {
        val idx = reminderList.indexOf(targetItem)
        reminderList[idx] =
            ReminderModel(
                reminderList[idx].id,
                inputName,
                inputYear,
                inputMonth,
                inputDay,
                inputTime,
                reminderList[idx].isChecked
            )

        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "id" to JsonPrimitive(targetItem.id),
                "name" to JsonPrimitive(inputName),
                "year" to JsonPrimitive(inputYear),
                "month" to JsonPrimitive(inputMonth),
                "day" to JsonPrimitive(inputDay),
                "time" to JsonPrimitive(inputTime)
            )
        )
        http.put("reminder", body)
    }

    fun removeReminderItem(targetItem: ReminderModel) {
        val http = MyHttp()
        http.delete("reminder", mapOf("id" to targetItem.id.toString()))
        reminderList.remove(targetItem)
    }

    fun checkReminderItem(targetItem: ReminderModel, value: Boolean) {
        val idx = reminderList.indexOf(targetItem)
        reminderList[idx] = ReminderModel(
            reminderList[idx].id,
            reminderList[idx].itemName,
            reminderList[idx].year,
            reminderList[idx].month,
            reminderList[idx].day,
            reminderList[idx].time,
            !reminderList[idx].isChecked
        )

        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "id" to JsonPrimitive(targetItem.id),
                "isChecked" to JsonPrimitive((!targetItem.isChecked).toString())
            )
        )
        http.put("reminder/checked", body)
    }

    fun getItemByIdx(idx: Int): ReminderModel {
        return reminderList[idx]
    }

    fun getItemIdx(item: ReminderModel): Int {
        return reminderList.indexOf(item)
    }
}
