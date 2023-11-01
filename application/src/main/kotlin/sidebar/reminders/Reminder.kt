package sidebar.reminders

import ReminderDataObject
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import files.File
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


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

    var reminderObjectList = mutableListOf<Reminder>();

    transaction {
        for (reminder in ReminderDataObject.selectAll()) {
            val reminderData = Reminder(reminder[ReminderDataObject.itemName]);
            reminderObjectList.add(reminderData)
        }
    }

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