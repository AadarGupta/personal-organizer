package sidebar.reminders

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ReminderContainer(currUser: MutableState<String>) {

    var ReminderVM = ReminderViewModel(currUser)
    var selectedItemIdx = remember { mutableStateOf(-1) }
    val dialogMode = remember { mutableStateOf("closed") }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:m a")
    var currTime = LocalDateTime.now().format(formatter)
    val alertMode = remember { mutableStateOf("closed") }
    var refreshDailyReminders = remember { mutableStateOf(false) }


    // pop up dialog for adding or editing a reminder item
    if (dialogMode.value == "add" || dialogMode.value == "edit") {
        ReminderDialog(dialogMode, selectedItemIdx.value, ReminderVM)
    }

    // check for reminders that are due today
    LaunchedEffect(Unit) {
        val refresh = withContext(Dispatchers.IO) {
            while (true) {
                delay( 5000)
                refreshDailyReminders.value = true
            }
        }
    }
    for ((index, it) in ReminderVM.getReminderList().withIndex()) {
        currTime = LocalDateTime.now().format(formatter)
        val dTime = it.year + "-" + it.month + "-" + it.day + " " + it.time
        if (currTime == dTime) {
            if(alertMode.value == "closed"){
                alertMode.value = index.toString()
            }
        }
        if (alertMode.value  == index.toString() ) {
            Alert(index, alertMode, ReminderVM)
            break;
        }
    }



    val state = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(){
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

            if (ReminderVM.isReminderEmpty()) {
                Text(
                    text = "No Reminders.", textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxHeight().align(Alignment.CenterHorizontally),
                    color = Color.White
                )
            } else {

                Text(
                    text = "Today's Reminders", textAlign = TextAlign.Center,
                    modifier = Modifier.padding()
                )
                if (ReminderVM.getTodayReminderList().size == 0) {
                    Text(
                        text = "No Reminders For Today.", textAlign = TextAlign.Center,
                        modifier = Modifier.padding()
                    )
                }
                else {
                    if (refreshDailyReminders.value) {
                        // used to refresh the reminders every 10s
                        refreshDailyReminders.value = false
                    }
                    LazyColumn {
                        items(ReminderVM.getTodayReminderList()) {
                            Card(
                                backgroundColor = Color.Gray,
                                modifier = Modifier
                                    .padding(2.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = it.itemName,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                                            color = Color.White,
                                        )
                                        Row() {
                                            Text(
                                                text = it.year + "." + it.month + "."+ it.day + ":"+ it.time ,
                                                fontSize = 10.sp,
                                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                                                color = Color.LightGray,
                                            )
                                            Icon(
                                                imageVector = Icons.Filled.Delete, contentDescription = "Delete",

                                                modifier = Modifier.clickable {
                                                    ReminderVM.removeReminderItem(it);
                                                },
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Text(
                    text = "All Reminders", textAlign = TextAlign.Center,
                    modifier = Modifier.padding()
                )

                LazyColumn(Modifier.padding(end = 12.dp), state) {
                    items(ReminderVM.getReminderList()) {
                        Card(
                            backgroundColor = Color(0xFFDBE9CF),
                            modifier = Modifier
                                .padding(start = 12.dp, top = 2.dp, bottom = 2.dp)
                                .clickable {
                                    selectedItemIdx.value = ReminderVM.getItemIdx(it)
                                    dialogMode.value = "edit"
                                },
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = it.itemName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                                        color = Color.DarkGray,
                                    )
                                    Row(modifier = Modifier.padding(end = 10.dp)) {
                                        Text(
                                            text = it.year + "." + it.month + "." + it.day + ":" + it.time,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                            color = Color.Gray,
                                        )
                                        Icon(
                                            imageVector = Icons.Filled.Delete, contentDescription = "Delete",

                                            modifier = Modifier.clickable {
                                                ReminderVM.removeReminderItem(it);
                                            },
                                            tint = Color(0xFF67c2b3)
                                        )
                                    }
                                }
                            }
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