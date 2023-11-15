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
                "time" to JsonPrimitive(time)
            )
        )

        val createReminderResponse = http.post("reminder", body)
        val newItem = Json.decodeFromString<ReminderModel>(createReminderResponse)
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
                inputTime
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

    fun removeReminderItemById(id: Int) {
        val http = MyHttp()
        http.delete("reminder", mapOf("id" to id.toString()))
        for(r in reminderList) {
            if(r.id == id){
                reminderList.remove(r)

            }
        }
    }

    fun getItemByIdx(idx: Int): ReminderModel {
        var temp : ReminderModel = reminderList[0]
        for(r in reminderList) {
            if(r.id == idx){
                return r;
            }
        }
        return temp
    }

    fun getItemIdx(item: ReminderModel): Int {
        return reminderList.indexOf(item)
    }
}
