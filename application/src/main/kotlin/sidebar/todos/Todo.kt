package sidebar.todos

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class ToDo(itemName: String) {
    var itemName = itemName
}

@Composable
fun ToDoItem(todo: ToDo) {
    Text(
        text = todo.itemName,
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
        color = Color.White,
    )
}

@Composable
fun ToDoContainer() {

    var todo1 = ToDo("Get groceries")
    var todo2 = ToDo("Study for MUSIC246 Midterm")
    var todo3 = ToDo("Plan a heist")
    var todo4 = ToDo("Learn how to walk")

    var todoObjectList = listOf(todo1, todo2, todo3, todo4)

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

        for (todoObject in todoObjectList) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                ToDoItem(todoObject)
            }
        }
    }
}