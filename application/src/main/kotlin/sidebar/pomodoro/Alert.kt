package sidebar.pomodoro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Alert(mode: MutableState<String> , pomodoroVM: PomodoroViewModel) {

    var pomodoroItem =  pomodoroVM.getPomodoro();
    var minutes = (pomodoroItem.worktime % 3600) / 60;
    var seconds = pomodoroItem.worktime % 60;

    var formattedWorkTime = String.format("%02d:%02d", minutes, seconds);

    minutes = (pomodoroItem.breaktime % 3600) / 60;
    seconds = pomodoroItem.breaktime % 60;

    var formattedBreakTime = String.format("%02d:%02d", minutes, seconds);

    AlertDialog(
        title = {
            if (mode.value == "wopen") {
                Text(
                    text = "Time to Start Work!", modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            if (mode.value == "bopen") {
                Text(
                    text = "Time to Take a Break!", modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        onDismissRequest = { mode.value = "closed" },
        confirmButton = {
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
                if(mode.value =="wopen"){
                    Text(
                        text = "$formattedWorkTime  MINUTES WORK STARTS NOW!",
                        modifier = Modifier.padding(20.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                if(mode.value =="bopen"){
                    Text(
                        text =  "$formattedBreakTime MINUTES BREAK STARTS NOW!",
                        modifier = Modifier.padding(20.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

        },
    )
}