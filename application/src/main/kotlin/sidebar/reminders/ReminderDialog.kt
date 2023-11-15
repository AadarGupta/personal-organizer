package sidebar.reminders

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
fun ReminderDialog(
    mode: MutableState<String>,
    reminderItemIdx: Int,
    reminderVM: ReminderViewModel,) {

    // default reminder item
    var reminderItem =
        ReminderModel(
            -1,
            "",
            "",
            "",
            "",
            ""
        )

    if (mode.value == "edit") {
        reminderItem = reminderVM.getItemByIdx(reminderItemIdx-1)
    }

    var name by remember { mutableStateOf(TextFieldValue(reminderItem.itemName)) };
    var year by remember { mutableStateOf(TextFieldValue(reminderItem.year))};
    var month by remember { mutableStateOf(TextFieldValue(reminderItem.month))};
    var day by remember { mutableStateOf(TextFieldValue(reminderItem.day))};
    var time by remember { mutableStateOf(TextFieldValue(reminderItem.time))};

    AlertDialog(
        title = {
            if (mode.value == "add") {
                Text(
                    text = "Edit Reminder", modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            if (mode.value == "edit") {
                Text(
                    text = "Edit Reminder", modifier = Modifier.padding(20.dp),
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
                        reminderVM.editReminderList(
                            reminderItem,
                            name.text,
                            year.text,
                            month.text,
                            day.text,
                            time.text
                        )
                    }
                    if (mode.value == "add") {
                        reminderVM.addReminderList(
                            name.text,
                            year.text,
                            month.text,
                            day.text,
                            time.text
                        )
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
                Text(
                    text = "Description" ,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp),
                    color = Color.DarkGray,
                )
                TextField(
                    value = name, onValueChange = { newText ->
                        name = newText
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, top= 5.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Text(
                    text = "Year" ,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp),
                    color = Color.DarkGray,
                )
                TextField(
                    value = year, onValueChange = { newText ->
                        year = newText
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, top= 5.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Text(
                    text = "Month" ,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp),
                    color = Color.DarkGray,
                )
                TextField(
                    value = month, onValueChange = { newText ->
                        month = newText
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, top= 5.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Text(
                    text = "Day" ,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp),
                    color = Color.DarkGray,
                )
                TextField(
                    value = day, onValueChange = { newText ->
                        day = newText
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, top= 5.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Text(
                    text = "Time" ,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp),
                    color = Color.DarkGray,
                )
                TextField(
                    value = time, onValueChange = { newText ->
                        time = newText
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, top= 5.dp),
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