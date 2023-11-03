package sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sidebar.reminders.ReminderContainer
import sidebar.todos.ToDoContainer

@Composable
fun SidebarContainer() {

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Box(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth(1f)
                .height(75.dp)
        ) {
            Text(
                text = "Toolbar",
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                color = Color.White,
            )
        }

        Box(
            modifier = Modifier
                .padding(10.dp)
                .border( width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .background(Color.Gray)
                .height(200.dp)
                .fillMaxWidth(1f)
                .padding(5.dp)


        ) {
            ToDoContainer()
        }

        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Gray)
                .border( width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .height(200.dp)
                .fillMaxWidth(1f)
                .padding(5.dp)

        ) {
            ReminderContainer()
        }
    }
}