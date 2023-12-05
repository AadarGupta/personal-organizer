package sidebar.reminders;

import MyHttp
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import io.ktor.client.call.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
// Internal list management of to do data objects
@Serializable
data class ReminderList(val items: List<ReminderModel>)
// Reminder viewmodel object
class ReminderViewModel(currUser: MutableState<String>) {
    var currUser = currUser
    private var reminderList = mutableStateListOf<ReminderModel>()

    init {
        runBlocking {
            launch {
                val http = MyHttp()
                val getAllReminderResponse = http.get("reminders?user=${currUser.value}")
                val obj = Json.decodeFromString<ReminderList>(getAllReminderResponse.body())

                for (reminder in obj.items) {
                    reminderList.add(reminder)
                }
            }
        }
    }

    //returns whole reminder list
    fun getReminderList() : List<ReminderModel> {
        return reminderList;
    }
    //returns list of reminders filtered by today's date
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
    //checks if reminder list is empty
    fun isReminderEmpty() : Boolean {
        return reminderList.isEmpty()
    }

    //adds to reminder list
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

        runBlocking {
            launch {
                val createReminderResponse = http.post("reminder?user=${currUser.value}", body)
                val newItem = Json.decodeFromString<ReminderModel>(createReminderResponse.body())
                reminderList.add(newItem)
            }
        }

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
                reminderList[idx].owner,
                inputName,
                inputYear,
                inputMonth,
                inputDay,
                inputTime,
                reminderList[idx].isChecked
            )

        runBlocking {
            launch {
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
                http.put("reminder?user=${currUser.value}", body)
            }
        }
    }
    //removes given object from list of reminders
    fun removeReminderItem(targetItem: ReminderModel) {
        runBlocking {
            launch {
                val http = MyHttp()
                http.delete(
                    "reminder",
                    mapOf(
                        "id" to targetItem.id.toString(),
                        "user" to currUser.value
                    )
                )
                reminderList.remove(targetItem)
            }
        }
    }

    //returns object filtered by index
    fun getItemByIdx(idx: Int): ReminderModel {
        return reminderList[idx]
    }

    // returns item index
    fun getItemIdx(item: ReminderModel): Int {
        return reminderList.indexOf(item)
    }
}
