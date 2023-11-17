package sidebar.pomodoro

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
fun PomodoroDialog(
    mode: MutableState<String>,
    pomodoroItemIdx: Int,
    pomodoroVM: PomodoroViewModel,) {

    var pomodoroItem = PomodoroModel(-1  , 0,0, false)
    if (mode.value == "edit") {
        pomodoroItem = pomodoroVM.getItemByIdx(pomodoroItemIdx)
    }

    AlertDialog(
        title = {
            if (mode.value == "edit") {
                Text(
                    text = "Timer Description", modifier = Modifier.padding(20.dp),
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
                        pomodoroVM.changePomodoroCheckStatus(pomodoroItem)
                    }
                    mode.value = "closed"
                }
            ) {
                Text("Start")
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
                    text = "Option 1: "+pomodoroItem.worktime+ " Min Work! " +pomodoroItem.breaktime+ " Min Break!",
                    modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

        },
    )
}