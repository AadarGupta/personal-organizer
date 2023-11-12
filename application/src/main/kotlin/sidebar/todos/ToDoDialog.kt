package sidebar.todos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToDoDialog(
    mode: MutableState<String>,
    toDoItemIdx: Int,
    toDoVM: ToDoViewModel,) {

    var toDoItem = ToDoModel(-1  , "", false)
    if (mode.value == "edit") {
        toDoItem = toDoVM.getItemByIdx(toDoItemIdx)
    }
    var item by remember { mutableStateOf(TextFieldValue(toDoItem.itemName)) }

    AlertDialog(
        title = {
            if (mode.value == "add") {
                Text(
                    text = "Add To Do Item", modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            if (mode.value == "edit") {
                Text(
                    text = "Edit To Do Item", modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        onDismissRequest = { mode.value = "closed" },
        confirmButton = {
            TextButton(
                onClick = {
                    if (mode.value == "edit") {
                        toDoVM.changeToDoName(toDoItem, item.text)
                    }
                    if (mode.value == "add") {
                        toDoVM.addToDo(item.text)
                    }
                    mode.value = "closed"
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { mode.value = "closed" }
            ) {
                Text("Dismiss")
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(10.dp),
            ) {

                TextField(
                    value = item, onValueChange = { newText ->
                        item = newText
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
            }

        },
    )
}