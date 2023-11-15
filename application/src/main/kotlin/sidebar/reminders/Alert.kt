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
fun Alert(
    reminderItemIdx: Int,
    mode: MutableState<String>,
    currTime: String,
    name: String,
    reminderVM: ReminderViewModel,) {

    var reminderItem = reminderVM.getItemByIdx(reminderItemIdx)

    AlertDialog(
        title = {
            Text(
                text = "Reminder!\n"+ name, modifier = Modifier.padding(20.dp),
                fontWeight = FontWeight.Bold,
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
                modifier = Modifier.padding(10.dp),
            ) {
                Text(
                    text = currTime ,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp),
                    color = Color.DarkGray,
                )

            }

        },
    )
}