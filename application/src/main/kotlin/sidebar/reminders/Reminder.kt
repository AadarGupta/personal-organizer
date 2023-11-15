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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReminderContainer() {

    var ReminderVM = ReminderViewModel()
    var selectedItemIdx = remember { mutableStateOf(-1) }
    val dialogMode = remember { mutableStateOf("closed") }

    // pop up dialog for adding or editing a reminder item
    if (dialogMode.value == "add" || dialogMode.value == "edit") {
        ReminderDialog(dialogMode, selectedItemIdx.value, ReminderVM)
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row() {
            Button(onClick = ({dialogMode.value = "add"}), shape = CircleShape, colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)) {
                Icon(
                    imageVector = Icons.Filled.Add, contentDescription = "Add",
                    tint = Color.White
                )
            }
        }

        if (ReminderVM.isReminderEmpty()) {
            Text(
                text = "No to do items.", textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxHeight().align(Alignment.CenterHorizontally),
                color = Color.White
            )
        } else {
            val state = rememberLazyListState()

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(Modifier.padding(end = 12.dp), state) {
                    items(ReminderVM.getReminderList()) {
                        Card(
                            backgroundColor = Color.LightGray,
                            modifier = Modifier
                                .padding(2.dp)
                                .clickable {
                                    selectedItemIdx.value = ReminderVM.getItemIdx(it)
                                    dialogMode.value = "edit"
                                }
                                .padding(vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Checkbox(
                                    checked = it.isChecked,
                                    onCheckedChange = { value -> ReminderVM.checkReminderItem(it, value) }
                                )
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
                                    Row() {
                                        Text(
                                            text = it.year + "." + it.month + "." + it.day + ":" + it.time,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                                            color = Color.Gray,
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