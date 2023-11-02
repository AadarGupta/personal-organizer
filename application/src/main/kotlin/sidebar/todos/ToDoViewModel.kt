package sidebar.todos;

import ToDoDataObject
import androidx.compose.runtime.mutableStateListOf
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class ToDoViewModel() {
    private var toDoList = mutableStateListOf<ToDoModel>()

    init {
        transaction {
            for (todo in ToDoDataObject.selectAll()) {
                val toDoData = ToDoModel(todo[ToDoDataObject.id], todo[ToDoDataObject.itemName], todo[ToDoDataObject.isChecked]);
                toDoList.add(toDoData)
            }
        }
    }

    fun getToDoList() : List<ToDoModel> {
        return toDoList;
    }

    fun isToDoEmpty() : Boolean {
        return toDoList.isEmpty()
    }

    fun addToDoList(toDoItem: ToDoModel) {
        toDoList.add(toDoItem)
    }

    fun removeToDoItem(toDoItem: ToDoModel) {

        toDoList.remove(toDoItem)
        transaction {
            ToDoDataObject.deleteWhere { ToDoDataObject.id eq toDoItem.id }
        }
    }

    fun checkToDoItem(toDoItem: ToDoModel, value: Boolean) {
        val idx = toDoList.indexOf(toDoItem)
        toDoList[idx].isChecked = value

        transaction {
            ToDoDataObject.update({ ToDoDataObject.id eq toDoItem.id }) {
                it[isChecked] = value;
            }
        }
    }
}
