package sidebar.todos

import ToDoDataObject
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class ToDo(id: EntityID<Int>, itemName: String, isChecked: Boolean) {
    var id = id
    var itemName = itemName
    var isChecked = isChecked
}

@Composable
fun ToDoItem(todo: ToDo) {
    Card(
        backgroundColor = Color.Gray,
        modifier = Modifier
            .padding(16.dp)
            .clickable {  }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Checkbox(checked = todo.isChecked, onCheckedChange = {checkTodo(!todo.isChecked, todo.id)})
            Text(
                text = todo.itemName,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                color = Color.White,
            )
            Icon(
                imageVector = Icons.Filled.Delete, contentDescription = "Delete",

                modifier = Modifier.clickable {
                    deleteTodo(todo.id)
                },
                tint = Color.Red
            )
        }
    }
}

@Composable
fun ToDoContainer() {

    var todoObjectList = getToDoList();

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .border( width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(10.dp)
                )

        ) {
            Text(
                text = "To-Dos",
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                color = Color.White,
            )
        }

        if (todoObjectList.isEmpty()) {
            Text(
                text = "No to do items.", textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxHeight()
            )
        } else {
            LazyColumn {
                items(todoObjectList) { todo ->
                    ToDoItem(todo)
                }
            }
        }
    }
}

fun getToDoList(): MutableList<ToDo> {
    var list = mutableListOf<ToDo>();

    transaction {
        for (todo in ToDoDataObject.selectAll()) {
            val toDoData = ToDo(todo[ToDoDataObject.id], todo[ToDoDataObject.itemName], todo[ToDoDataObject.isChecked]);
            list.add(toDoData)
        }
    }

    return list;
}

fun deleteTodo(todoId: EntityID<Int>) {
    transaction {
        ToDoDataObject.deleteWhere { ToDoDataObject.id eq todoId }
    }
}
fun checkTodo(updateValue: Boolean, todoId: EntityID<Int>) {
    transaction {
        ToDoDataObject.update({ ToDoDataObject.id eq todoId }) {
            it[isChecked] = updateValue;
        }
    }
}