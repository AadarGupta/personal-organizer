package sidebar.todos;

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
data class ToDoList(val items: List<ToDoModel>)

// To do viewmodel object
class ToDoViewModel(currUser: MutableState<String>) {
    var currUser = currUser
    private var toDoList = mutableStateListOf<ToDoModel>()

    init {
        runBlocking {
            launch {
                val http = MyHttp()
                val getAllToDoResponse = http.get("todos?user=${currUser.value}")
                val obj = Json.decodeFromString<ToDoList>(getAllToDoResponse.body())

                for (todo in obj.items) {
                    toDoList.add(todo)
                }
            }
        }
    }

    // Returns all to do items in internal list.
    fun getToDoList() : List<ToDoModel> {
        return toDoList;
    }

    // Returns whether internal to do list is empty or not.
    fun isToDoEmpty() : Boolean {
        return toDoList.isEmpty()
    }

    // Creates and adds a to do item with name itemName.
    fun addToDo(itemName: String) : Int {
        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "name" to JsonPrimitive(itemName),
                "isChecked" to JsonPrimitive(false)
            )
        )

        runBlocking {
            launch {
                val createToDoResponse = http.post("todo?user=${currUser.value}", body)
                val newItem = Json.decodeFromString<ToDoModel>(createToDoResponse.body())
                toDoList.add(newItem)
            }
        }

        return toDoList.size - 1
    }

    // Edits the name of to do object targetItem to newName.
    fun changeToDoName(targetItem: ToDoModel, newName: String) {
        val idx = toDoList.indexOf(targetItem)
        toDoList[idx] =
            ToDoModel(
                toDoList[idx].id,
                toDoList[idx].owner,
                newName,
                toDoList[idx].isChecked
            )

        runBlocking {
            launch {
                val http = MyHttp()
                val body = JsonObject(
                    mapOf(
                        "id" to JsonPrimitive(targetItem.id),
                        "name" to JsonPrimitive(newName)
                    )
                )
                http.put("todo/name?user=${currUser.value}", body)
            }
        }
    }

    // Edits the checked boolean of targetItem.
    fun changeToDoCheckStatus(targetItem: ToDoModel) {
        val idx = toDoList.indexOf(targetItem)
        toDoList[idx] = ToDoModel(toDoList[idx].id, toDoList[idx].owner, toDoList[idx].itemName, !toDoList[idx].isChecked)

        runBlocking {
            launch {
                val http = MyHttp()
                val body = JsonObject(
                    mapOf(
                        "id" to JsonPrimitive(targetItem.id),
                        "isChecked" to JsonPrimitive((!targetItem.isChecked).toString())
                    )
                )
                http.put("todo/checked?user=${currUser.value}", body)
            }
        }
    }

    // Removes to do item targetItem.
    fun removeToDoItem(targetItem: ToDoModel) {
        runBlocking {
            launch {
                val http = MyHttp()
                http.delete(
                    "todo",
                    mapOf(
                        "id" to targetItem.id.toString(),
                        "user" to currUser.value
                    )
                )
                toDoList.remove(targetItem)
            }
        }
    }

    // Returns index of to do item toDoItem in internal list.
    fun getIdxById(toDoItem: ToDoModel): Int {
        return toDoList.indexOf(toDoItem)
    }

    // Returns the to do item stored at idx of internal list.
    fun getItemByIdx(idx: Int): ToDoModel {
        return toDoList[idx]
    }
}
