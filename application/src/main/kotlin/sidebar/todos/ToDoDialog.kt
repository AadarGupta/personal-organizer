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
    active: MutableState<Boolean>,
    toDoItemIdx: Int,
    toDoVM: ToDoViewModel,) {

    var toDoItem = toDoVM.getItemByIdx(toDoItemIdx)

    var item by remember { mutableStateOf(TextFieldValue(toDoItem.itemName)) }

    AlertDialog(
        title = {
            Text(
                text = "Edit To Do Item", modifier = Modifier.padding(20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        onDismissRequest = { active.value = false },
        confirmButton = {
            TextButton(
                onClick = {
                    toDoVM.editToDoList(toDoItem, item.text)
                    active.value = false
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { active.value = false }
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