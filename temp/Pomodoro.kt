package sidebar.pomodoro

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollbarAdapter
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


@Composable
fun PomodoroContainer(currUser: MutableState<String>) {

    var pomodoroVM = PomodoroViewModel(currUser)
    var working = remember { mutableStateOf(true) }
    var breaking = remember { mutableStateOf(false) }
    var selectedItemIdx = remember { mutableStateOf(1) }
    val dialogMode = remember { mutableStateOf("closed") }
    val workingDialogMode = remember { mutableStateOf("closed") }
    val breakingDialogMode = remember { mutableStateOf("closed") }


    LaunchedEffect(Unit) {
        val refresh = withContext(Dispatchers.IO) {
            while (true) {
                if (working.value) {
                    workingDialogMode.value = "wopen"
                    delay(100000)
                    workingDialogMode.value = "closed"
                    breakingDialogMode.value = "bopen"

                    println("t1")
                    println("w" + working.value)
                    working.value = false
                    breaking.value = true
                    println("w" + working.value)
                    println("b" + breaking.value)

                }
                if (breaking.value) {
                    println("t2")
                    println("b" + breaking.value)
                    breakingDialogMode.value = "bopen"
                    delay(100000)
                    breakingDialogMode.value = "closed"
                    workingDialogMode.value = "wopen"
                    working.value = true
                    breaking.value = false
                    println("w" + working.value)
                    println("b" + breaking.value)
                }

            }
        }
    }
    // pop up dialog for adding or editing a pomodoro item
    if (dialogMode.value == "edit") {
        PomodoroDialog(dialogMode, selectedItemIdx.value, pomodoroVM)
    }
    if (working.value && workingDialogMode.value == "wopen") {
        println("hello?1")
        println(selectedItemIdx.value)
        Alert(workingDialogMode, selectedItemIdx.value , pomodoroVM)
    }
    if (breaking.value && breakingDialogMode.value == "bopen") {
        println("hello?2")
        Alert(breakingDialogMode, selectedItemIdx.value, pomodoroVM)
    }
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row() {
            Button(onClick = ({dialogMode.value = "add"}),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                modifier = Modifier.padding(start = 12.dp)) {
                Icon(
                    imageVector = Icons.Filled.Add, contentDescription = "Add",
                    tint = Color.White
                )
            }
        }

        if (pomodoroVM.isPomodoroEmpty()) {
            Text(
                text = "No to do items.", textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxHeight().align(Alignment.CenterHorizontally),
                color = Color.White
            )
        } else {
            val state = rememberLazyListState()

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(Modifier.padding(end = 12.dp), state) {
                    items(pomodoroVM.getPomodoroList()) {
                        Card(
                            backgroundColor = Color(0xFFDBE9CF),
                            modifier = Modifier
                                .clickable {
                                    selectedItemIdx.value = pomodoroVM.getIdxById(it)
                                    dialogMode.value = "edit"
                                }
                                .padding(start = 12.dp, top = 2.dp, bottom = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                            ) {
                                Checkbox(
                                    checked = it.isChecked,
                                    onCheckedChange = { value ->
                                        workingDialogMode.value = "closed"
                                        breakingDialogMode.value = "bopen"
                                    }
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Option 1: "+it.worktime+ " Min Work! " +it.breaktime+ " Min Break!",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                                        color = Color.DarkGray,
                                    )
                                }
                            }
                        }
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(
                        scrollState = state
                    )
                )
            }
        }
    }
}