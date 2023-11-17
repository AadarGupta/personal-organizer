package sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sidebar.reminders.ReminderContainer
import sidebar.todos.ToDoContainer

@Composable
fun SidebarContainer() {
    var todoVisible by remember { mutableStateOf(true) }
    var remindersVisible by remember { mutableStateOf(true) }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Column (
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth(1f)
                .height(80.dp)
        ) {
            Text(
                text = "Widget Bar",
                fontSize = 30.sp,
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp ),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier.width(350.dp)
                    .height(2.dp)
                    .background(color = Color.White)
                    .align(Alignment.CenterHorizontally)
            )
        }
        TextButton(onClick = ({ todoVisible = !todoVisible })) {
            Text(
                text = "> To-Do",
                fontSize = 15.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(start = 5.dp)
            )
        }
        AnimatedVisibility(todoVisible) {
            Box(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(Color.Transparent)
                    .height(200.dp)
                    .fillMaxWidth(1f)
                    .padding(5.dp)
            ) {
                ToDoContainer()
            }
        }

        TextButton(onClick = ({ remindersVisible = !remindersVisible })) {
            Text(
                text = "> Reminders",
                fontSize = 15.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(start = 5.dp)
            )
        }
        AnimatedVisibility(remindersVisible) {
            Box(
                modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .clip(shape = RoundedCornerShape(10.dp))
                .background(Color.Transparent)
                .height(200.dp)
                .fillMaxWidth(1f)
                .padding(5.dp)
            ) {
                ReminderContainer()
            }
        }
    }
}