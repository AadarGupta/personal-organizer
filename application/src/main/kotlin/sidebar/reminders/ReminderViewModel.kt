package sidebar.reminders;

import MyHttp
import androidx.compose.runtime.mutableStateListOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class ReminderList(val items: List<ReminderModel>)

class ReminderViewModel() {
    private var reminderList = mutableStateListOf<ReminderModel>()
    private var todayReminderList = mutableStateListOf<ReminderModel>()

    //    var currTime = remember { mutableStateOf(LocalDateTime.now().format(formatter))}
    init {
        val http = MyHttp()
        val getAllReminderResponse: String = http.get("reminders")
        println(getAllReminderResponse)
        val obj = Json.decodeFromString<ReminderList>(getAllReminderResponse)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var today = LocalDateTime.now().format(formatter)
        for (reminder in obj.items) {
            reminderList.add(reminder)
            val temp = reminder.year + "-" + reminder.month + "-" + reminder.day
            if (today == temp) {
                todayReminderList.add(reminder)
            }
        }
    }

    fun getReminderList(): List<ReminderModel> {
        return reminderList;
    }

    fun getTodayReminderList(): List<ReminderModel> {
        return todayReminderList;
    }

    fun isReminderEmpty(): Boolean {
        return reminderList.isEmpty()
    }

    fun isTodayReminderEmpty(): Boolean {
        return todayReminderList.isEmpty()
    }

    fun addReminderList(
        itemName: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): Int {
        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "name" to JsonPrimitive(itemName),
                "year" to JsonPrimitive(year),
                "month" to JsonPrimitive(month),
                "day" to JsonPrimitive(day),
                "time" to JsonPrimitive(time)
            )
        )

        val createReminderResponse = http.post("reminder", body)
        val newItem = Json.decodeFromString<ReminderModel>(createReminderResponse)
        reminderList.add(newItem)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var today = LocalDateTime.now().format(formatter)
        val temp = newItem.year + "-" + newItem.month + "-" + newItem.day
        if (today == temp) {
            todayReminderList.add(newItem)

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
                inputName,
                inputYear,
                inputMonth,
                inputDay,
                inputTime
            )
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var today = LocalDateTime.now().format(formatter)
        val temp = inputYear + "-" + inputMonth + "-" + inputDay
        val idx2 = todayReminderList.indexOf(targetItem)
        if (today == temp) {
            if(idx2 == -1){
                println(todayReminderList);
                todayReminderList.add(
                    reminderList[idx]
                )
                println(todayReminderList);

            }else{
                todayReminderList[idx2] =
                    ReminderModel(
                        todayReminderList[idx2].id,
                        inputName,
                        inputYear,
                        inputMonth,
                        inputDay,
                        inputTime
                    )
            }
        }else{
            if(idx2 != -1) {
                todayReminderList.removeAt(idx2)
            }

        }

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
        todayReminderList.remove(targetItem)
    }

    fun removeReminderItemById(id: Int) {
        val http = MyHttp()
        http.delete("reminder", mapOf("id" to id.toString()))
        for (r in reminderList) {
            if (r.id == id) {
                reminderList.remove(r)

            }
        }
        for (r in todayReminderList) {
            if (r.id == id) {
                todayReminderList.remove(r)

            }
        }
    }
    fun getItemByIdx(idx: Int): ReminderModel {
        var temp: ReminderModel = reminderList[0]
        for (r in reminderList) {
            if (r.id == idx) {
                return r;
            }
        }
        return reminderList[idx]
    }

//    fun getItemByIdx(idx: Int): ReminderModel {
//        var temp: ReminderModel = reminderList[0]
//        for (r in reminderList) {
//            if (r.id == idx) {
//                return r;
//            }
//        }
//        return temp
//    }

    fun getTodayItemByIdx(idx: Int): ReminderModel {
        var temp: ReminderModel = todayReminderList[0]
        for (r in todayReminderList) {
            if (r.id == idx) {
                return r;
            }
        }
        return temp
    }

    fun getItemIdx(item: ReminderModel): Int {
        return reminderList.indexOf(item)
    }
    fun getTodayItemIdx(item: ReminderModel): Int {
        return todayReminderList.indexOf(item)
    }
}