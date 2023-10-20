package sidebar.reminders

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class Reminder(itemName: String) {
    var itemName = itemName
}

@Composable
fun ReminderItem(reminder: Reminder) {
    Text(
        text = reminder.itemName,
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
        color = Color.White,
    )
}

@Composable
fun RemindersContainer() {

    var reminder1 = Reminder("Pay hydro")
    var reminder2 = Reminder("Pay rent")
    var reminder3 = Reminder("Go to bed earlier")

    var reminderObjectList = listOf(reminder1, reminder2, reminder3)

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
                text = "Reminders",
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                color = Color.White,
            )
        }

        for (reminderObject in reminderObjectList) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                ReminderItem(reminderObject)
            }
        }
    }
}