package sidebar.todos;

import MyHttp
import androidx.compose.runtime.mutableStateListOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class ToDoList(val items: List<ToDoModel>)

class ToDoViewModel() {
    private var toDoList = mutableStateListOf<ToDoModel>()

    init {
        val http = MyHttp()
        val getAllToDoResponse : String = http.get("todos")
        println(getAllToDoResponse)
        val obj = Json.decodeFromString<ToDoList>(getAllToDoResponse)

        for (todo in obj.items) {
            toDoList.add(todo)
        }
    }

    fun getToDoList() : List<ToDoModel> {
        return toDoList;
    }

    fun isToDoEmpty() : Boolean {
        return toDoList.isEmpty()
    }

    fun addToDo(itemName: String) : Int {
        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "name" to JsonPrimitive(itemName),
                "isChecked" to JsonPrimitive(false)
            )
        )
        val createToDoResponse = http.post("todo", body)
        val newItem = Json.decodeFromString<ToDoModel>(createToDoResponse)
        toDoList.add(newItem)

        return newItem.id
    }

    fun changeToDoName(targetItem: ToDoModel, newName: String) {
        val idx = toDoList.indexOf(targetItem)
        toDoList[idx] = ToDoModel(toDoList[idx].id, newName, toDoList[idx].isChecked)

        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "id" to JsonPrimitive(targetItem.id),
                "name" to JsonPrimitive(newName)
            )
        )
        http.put("todo/name", body)
    }

    fun changeToDoCheckStatus(targetItem: ToDoModel) {
        val idx = toDoList.indexOf(targetItem)
        toDoList[idx] = ToDoModel(toDoList[idx].id, toDoList[idx].itemName, !toDoList[idx].isChecked)

        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "id" to JsonPrimitive(targetItem.id),
                "isChecked" to JsonPrimitive((!targetItem.isChecked).toString())
            )
        )
        http.put("todo/checked", body)
    }

    fun removeToDoItem(targetItem: ToDoModel) {
        val http = MyHttp()
        http.delete("todo", mapOf("id" to targetItem.id.toString()))
        toDoList.remove(targetItem)
    }

    fun getIdxById(toDoItem: ToDoModel): Int {
        return toDoList.indexOf(toDoItem)
    }

    fun getItemByIdx(idx: Int): ToDoModel {
        return toDoList[idx]
    }
}
