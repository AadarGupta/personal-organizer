package sidebar.pomodoro

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.runtime.MutableState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import sidebar.reminders.ReminderViewModel


@Composable
fun PomodoroContainer(currUser: MutableState<String>) {
    var pomodoroVM = PomodoroViewModel()
    var working = remember { mutableStateOf(true) }
    var breaking = remember { mutableStateOf(false) }
    var selectedItemIdx = remember { mutableStateOf(1) }
    val workingDialogMode = remember { mutableStateOf("closed") }
    val breakingDialogMode = remember { mutableStateOf("closed") }
    var timeLeft by remember { mutableStateOf(pomodoroVM.getPomodoro().worktime) }


    var timeLeftString by remember { mutableStateOf("25:00") }
    var isPaused by remember { mutableStateOf(true) }
    var isStart by remember { mutableStateOf(true) }
    var breakTime by remember { mutableStateOf(TextFieldValue("")) }
    var workTime by remember { mutableStateOf(TextFieldValue("")) }
    var breakTimeExpanded = remember { mutableStateOf(false) }
    var workTimeExpanded = remember { mutableStateOf(false) }


    if (breaking.value && breakingDialogMode.value == "bopen") {
        Alert(breakingDialogMode,pomodoroVM)
    }

    if (working.value && workingDialogMode.value == "wopen") {
        Alert(workingDialogMode,pomodoroVM)
    }


    LaunchedEffect(key1 = timeLeft, key2 = isPaused) {
        while (timeLeft > 0 && !isPaused) {
            delay(1000L)
            timeLeft--
            var minutes = (timeLeft % 3600) / 60;
            var seconds = timeLeft % 60;

            var timeString = String.format("%02d:%02d", minutes, seconds);
            timeLeftString = timeString;
            var curr = pomodoroVM.getPomodoro();
            if(timeLeft == 0){
                if (working.value) {
                    working.value = false
                    breaking.value = true
                    workingDialogMode.value = "closed"
                    breakingDialogMode.value = "bopen"
                    timeLeft = curr.breaktime
                    isPaused = false
                    isStart = false
                }else{
                    working.value = true
                    breaking.value = false
                    breakingDialogMode.value = "closed"
                    workingDialogMode.value = "wopen"
                    timeLeft = curr.worktime
                    isPaused = false
                    isStart = false
                }
            }
        }
    }
    var curr = pomodoroVM.getPomodoro();
    fun resetTimer() {
        if(working.value) {
            timeLeft = curr.worktime
        }else{
            timeLeft = curr.breaktime
        }
        if(breaking.value){
            timeLeft = curr.breaktime
        }else{
            timeLeft = curr.worktime
        }
        isPaused = true
        isStart = true
        workingDialogMode.value = "closed"
        breakingDialogMode.value = "closed"

        var minutes = (timeLeft % 3600) / 60;
        var seconds = timeLeft % 60;

        var timeString = String.format("%02d:%02d", minutes, seconds);
        timeLeftString = timeString;
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TIME TO " + if(working.value) "STUDY!" else "TAKE A BREAK!",
            color = Color.White,
            fontWeight = FontWeight.W500,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 2.5.dp),
            )
        Box(
            modifier = Modifier.background(Color.Black,  shape = RoundedCornerShape(8.dp))
                .height(80.dp)
                .width(210.dp)
                .border(4.dp, Color.Green, shape = RoundedCornerShape(8.dp))
                .padding(vertical = 5.dp, horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text= timeLeftString ,
                color = Color.Green,
                fontWeight = FontWeight.W800,
                fontSize = 45.sp,
                )
        }
//        Column() {
//            Text(
//                text = "Worktime" ,
//                textAlign = TextAlign.Center,
//                fontSize = 12.sp,
//                modifier = Modifier.width(150.dp).padding(horizontal = 4.dp, vertical = 0.dp),
//                color = Color.DarkGray,
//            )
//            Box {
//                TextButton(
//                    onClick = { workTimeExpanded.value = !workTimeExpanded.value },
//                    modifier = Modifier.width(150.dp)
//                ) {
//                    Text(text = workTime.text, textAlign = TextAlign.Center)
//                }
//
//                DropdownMenu(
//                    expanded = workTimeExpanded.value,
//                    onDismissRequest = { workTimeExpanded.value = !workTimeExpanded.value },
//                ) {
//                    for (i in 0..59) {
//                        DropdownMenuItem(
//                            onClick = {
//                                workTime = TextFieldValue(i.toString())
//                                workTimeExpanded.value = false
//                                pomodoroVM.editWorkTime(i)
//                            },
//                            modifier = Modifier.width(150.dp)
//                        ) {
//                            Text(i.toString())
//                        }
//                    }
//                }
//            }
//        }
        Row (
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.5.dp),
        ){
            Button(onClick = {
                isPaused = !isPaused
                if(isStart){
                    if(working.value){
                        breakingDialogMode.value = "closed"
                        workingDialogMode.value = "wopen"
                    }else{
                        breakingDialogMode.value = "bopen"
                        workingDialogMode.value = "closed"
                    }
                }
                isStart = false

            },
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp).width(100.dp),
            ) {
                Text(text = if (isPaused) (if(isStart)"Start" else "Resume")else "Pause")
            }
            Button(
                onClick = { resetTimer() },
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp).width(100.dp),
            ) {
                Text(text = "Reset")
            }
        }
    }
}