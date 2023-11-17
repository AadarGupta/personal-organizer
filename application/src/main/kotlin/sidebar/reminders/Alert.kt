package sidebar.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Alert(
    reminderItemIdx: Int,
    mode: MutableState<String>,
    reminderVM: ReminderViewModel,) {
    var reminderItem = reminderVM.getItemByIdx(reminderItemIdx)
    var name by remember { mutableStateOf(reminderItem.itemName)};
    var year by remember { mutableStateOf(reminderItem.year)};
    var month by remember { mutableStateOf(reminderItem.month)};
    var day by remember { mutableStateOf(reminderItem.day)};
    var time by remember { mutableStateOf(reminderItem.time)};

    AlertDialog(

        title = {
            Text(
                text = "You have a Reminder!\n\n"+ name, modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )

        },
        onDismissRequest = { mode.value = "closed" },
        confirmButton = {
        },
        dismissButton = {
            TextButton(
                onClick = {
                    reminderVM.removeReminderItem(reminderItem);
                    mode.value = "closed"
                }
            ) {
                Text("Dismiss")
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(5.dp),
            ) {
                Text(
                    text =  "at " + year + "-" + month  + "-" + day  + " " + time
                    ,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 25.dp, vertical = 0.dp),
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,

                    )

            }

        },
    )
}