package sidebar.pomodoro

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.runtime.MutableState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun PomodoroContainer(currUser: MutableState<String>) {
    var pomodoroVM = PomodoroViewModel()
    var working = remember { mutableStateOf(true) }
    var breaking = remember { mutableStateOf(false) }
    val workingDialogMode = remember { mutableStateOf("closed") }
    val breakingDialogMode = remember { mutableStateOf("closed") }
    var timeLeft by remember { mutableStateOf(pomodoroVM.getPomodoro().worktime) }

    var minutes2 = (timeLeft % 3600) / 60;
    var seconds2 = timeLeft % 60;

    var timeString2 = String.format("%02d:%02d", minutes2, seconds2);

    var timeLeftString by remember {(mutableStateOf(timeString2))}
    var isPaused by remember { mutableStateOf(true) }
    var isStart by remember { mutableStateOf(true) }
    var workTime by remember { mutableStateOf(timeString2) }

    minutes2 = (pomodoroVM.getPomodoro().breaktime % 3600) / 60;
    seconds2 = pomodoroVM.getPomodoro().breaktime % 60;

    timeString2 = String.format("%02d:%02d", minutes2, seconds2);

    var breakTime by remember { mutableStateOf(timeString2) }
    var workTimeInt by remember { mutableStateOf(pomodoroVM.getPomodoro().worktime) }
    var breakTimeInt by remember { mutableStateOf(pomodoroVM.getPomodoro().breaktime) }
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
                    timeLeft = breakTimeInt
                    isPaused = false
                    isStart = false
                }else{
                    working.value = true
                    breaking.value = false
                    breakingDialogMode.value = "closed"
                    workingDialogMode.value = "wopen"
                    timeLeft = workTimeInt
                    isPaused = false
                    isStart = false
                }
            }
        }
    }
    fun resetTimer() {
        if(working.value){
            timeLeft = workTimeInt
        }else{
            timeLeft = breakTimeInt
        }
        if(breaking.value){
            timeLeft = breakTimeInt
        }else{
            timeLeft = workTimeInt
        }
        isPaused = true
        isStart = true
        workingDialogMode.value = "closed"
        breakingDialogMode.value = "closed"

        val minutes = (timeLeft % 3600) / 60;
        val seconds = timeLeft % 60;

        val timeString = String.format("%02d:%02d", minutes, seconds);
        timeLeftString = timeString;
    }

    fun editTimer(timeLeft:Int): String {
        val minutes = (timeLeft % 3600) / 60;
        val seconds = timeLeft % 60;

        val timeString = String.format("%02d:%02d", minutes, seconds);
        return timeString;
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
        Row(){
            Column() {
                Text(
                    text = "Work Time" ,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    modifier = Modifier.width(150.dp).padding(horizontal = 4.dp, vertical = 0.dp),
                    color = Color.DarkGray,
                )
                Box {
                    TextButton(
                        onClick = { workTimeExpanded.value = !workTimeExpanded.value },
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text(text = workTime, textAlign = TextAlign.Center)
                    }

                    DropdownMenu(
                        expanded = workTimeExpanded.value,
                        onDismissRequest = { workTimeExpanded.value = !workTimeExpanded.value },
                    ) {
                        for (i in 0..59) {
                            DropdownMenuItem(
                                onClick = {
                                    pomodoroVM.editWorkTime(i*60)
                                    workTimeExpanded.value = false
                                    if(!breaking.value){
                                        timeLeftString = editTimer(i*60)
                                    }
                                    workTime = editTimer(i*60)
                                    workTimeInt = i*60
                                    resetTimer()

                                },
                                modifier = Modifier.width(150.dp)
                            ) {
                                Text(i.toString())
                            }
                        }
                    }
                }
            }
            Column {
                Text(
                    text = "Break Time" ,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    modifier = Modifier.width(150.dp).padding(horizontal = 4.dp, vertical = 0.dp),
                    color = Color.DarkGray,
                )
                Box {
                    TextButton(
                        onClick = { breakTimeExpanded.value = !breakTimeExpanded.value },
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text(text =  breakTime, textAlign = TextAlign.Center)
                    }

                    DropdownMenu(
                        expanded = breakTimeExpanded.value,
                        onDismissRequest = { breakTimeExpanded.value = !breakTimeExpanded.value },
                    ) {
                        for (i in 0..59) {
                            DropdownMenuItem(
                                onClick = {
                                    pomodoroVM.editBreakTime(i*60)
                                    breakTimeExpanded.value = false
                                    if(breaking.value){
                                        timeLeftString = editTimer(i*60)
                                    }
                                    breakTime = editTimer(i*60)
                                    breakTimeInt = i*60
                                    resetTimer()

                                },
                                modifier = Modifier.width(150.dp)
                            ) {
                                Text(i.toString())
                            }
                        }
                    }
                }
            }
        }


        Row {
            Box(
                modifier = Modifier.background(Color.Black,  shape = RoundedCornerShape(8.dp))
                    .height(100.dp)
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
            Column{
                Button(
                    onClick = {
                        isPaused = !isPaused
                        if (isStart) {
                            if (working.value) {
                                breakingDialogMode.value = "closed"
                                workingDialogMode.value = "wopen"
                            } else {
                                breakingDialogMode.value = "bopen"
                                workingDialogMode.value = "closed"
                            }
                        }
                        isStart = false

                    },
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp).width(100.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF67c2b3))
                ) {
                    Text(text = if (isPaused) (if (isStart) "Start" else "Resume") else "Pause", color = Color.White)
                }
                Button(
                    onClick = { resetTimer() },
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp).width(100.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF67c2b3))
                ) {
                    Text(text = "Reset", color = Color.White)
                }
            }
        }
    }
}