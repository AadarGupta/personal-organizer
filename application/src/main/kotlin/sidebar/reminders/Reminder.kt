package sidebar.reminders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        Row(
            modifier = Modifier
        ) {
            Text(
                text = "Reminders",
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                color = Color.White,
            )
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = "Add",

                modifier = Modifier
                    .clickable {
                        dialogMode.value = "add"
                    }.padding(vertical = 4.dp),
                tint = Color.White
            )

        }

        if (ReminderVM.isReminderEmpty()) {
            Text(
                text = "No to do items.", textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxHeight()
            )
        } else {
            LazyColumn {
                items(ReminderVM.getReminderList()) {
                    Card(
                        backgroundColor = Color.Gray,
                        modifier = Modifier
                            .padding(2.dp)
                            .clickable {
                                selectedItemIdx.value = ReminderVM.getItemIdx(it)
                                dialogMode.value = "edit"
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Checkbox(
                                checked = it.isChecked,
                                onCheckedChange = { value -> ReminderVM.checkReminderItem(it, value)}
                            )
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
    }
}