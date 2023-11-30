package sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sidebar.pomodoro.PomodoroContainer
import sidebar.reminders.ReminderContainer
import sidebar.todos.ToDoContainer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SidebarContainer(currUser: MutableState<String>) {
    var todoVisible by remember { mutableStateOf(true) }
    var remindersVisible by remember { mutableStateOf(true) }
    var pomodoroVisible by remember { mutableStateOf(true) }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Column (
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth(1f)
                .height(70.dp)
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
        TextButton(onClick = ({ pomodoroVisible = !pomodoroVisible })) {
            Text(
                text = "> Pomodoro",
                fontSize = 15.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(start = 5.dp)
            )
        }
        AnimatedVisibility(pomodoroVisible) {
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
                    .height(220.dp)
                    .fillMaxWidth(1f)
                    .padding(5.dp)
            ) {
                PomodoroContainer(currUser)
            }
        }

        Row() {
            TextButton(onClick = ({ todoVisible = !todoVisible })) {
                Text(
                    text = "> To-Do",
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = 5.dp)
                )
                TooltipArea(
                    tooltip = {
                        // composable tooltip content
                        Surface(

                            color = Color.Gray.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Widget for organizing tasks. Create, check, edit and delete todo tasks. Click to collapse the widget.",
                                modifier = Modifier.padding(10.dp), color = Color.Black
                            )
                        }
                    },
                    modifier = Modifier.padding(start = 10.dp),
                    delayMillis = 600, // in milliseconds
                    tooltipPlacement = TooltipPlacement.CursorPoint(
                        alignment = Alignment.BottomEnd
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info, contentDescription = "Todo Tooltip",
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
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
                ToDoContainer(currUser)
            }
        }
        Row() {
            TextButton(onClick = ({ remindersVisible = !remindersVisible })) {
                Text(
                    text = "> Reminders",
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = 5.dp)
                )
                TooltipArea(
                    tooltip = {
                        // composable tooltip content
                        Surface(

                            color = Color.Gray.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Widget for setting reminders. At the set date & time, an in-app reminder will pop up. Click to collapse the widget.",
                                modifier = Modifier.padding(10.dp), color = Color.Black
                            )
                        }
                    },
                    modifier = Modifier.padding(start = 10.dp),
                    delayMillis = 600, // in milliseconds
                    tooltipPlacement = TooltipPlacement.CursorPoint(
                        alignment = Alignment.BottomEnd
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info, contentDescription = "Todo Tooltip",
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
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
                .height(300.dp)
                .fillMaxWidth(1f)
                .padding(5.dp)
            ) {
                ReminderContainer(currUser)
            }
        }
    }
}