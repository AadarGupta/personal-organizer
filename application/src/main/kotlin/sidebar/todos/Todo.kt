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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import sidebar.reminders.Reminder
import javax.sql.rowset.RowSetProvider


class ToDo(itemName: String, isChecked: Boolean) {
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
            Checkbox(checked = todo.isChecked, onCheckedChange = {})
            Text(
                text = todo.itemName,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                color = Color.White,
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
            val toDoData = ToDo(todo[ToDoDataObject.itemName], todo[ToDoDataObject.isChecked]);
            list.add(toDoData)
        }
    }

    return list;
}