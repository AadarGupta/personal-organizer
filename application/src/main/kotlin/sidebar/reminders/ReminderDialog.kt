package sidebar.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReminderDialog(
    mode: MutableState<String>,
    reminderItemIdx: Int,
    reminderVM: ReminderViewModel,) {

    // default reminder item
    var reminderItem =
        ReminderModel(
            -1,
            "",
            "",
            "",
            "",
            "",
            "",
            false
        )

    if (mode.value == "edit") {
        reminderItem = reminderVM.getItemByIdx(reminderItemIdx)
    }

    var name by remember { mutableStateOf(TextFieldValue(reminderItem.itemName))};

    var year by remember { mutableStateOf(TextFieldValue(reminderItem.year))};
    var month by remember { mutableStateOf(TextFieldValue(reminderItem.month))};
    var day by remember { mutableStateOf(TextFieldValue(reminderItem.day))};

    var today = java.time.LocalTime.now()
    var amPMdefault = "a.m."
    var hourDefault = today.hour.toString()
    if (today.hour > 12) {
        hourDefault = (today.hour - 12).toString()
        amPMdefault = "p.m."
    }
    var timeSplit = mutableListOf(hourDefault, today.minute.toString(), amPMdefault)
    if (reminderItem.time != "") {
        reminderItem.time.substring(reminderItem.time.length-2, reminderItem.time.length).split(":").toMutableList()
        timeSplit.add(reminderItem.time.substring(reminderItem.time.length-2, reminderItem.time.length))
    }

    var hour by remember { mutableStateOf(TextFieldValue(timeSplit[0])) };
    var minute by remember { mutableStateOf(TextFieldValue(timeSplit[1])) };
    var amPM by remember { mutableStateOf(TextFieldValue(timeSplit[2])) };

    var time by remember { mutableStateOf(TextFieldValue(reminderItem.time))};

    var dayExpanded = remember { mutableStateOf(false) }
    var monthExpanded = remember { mutableStateOf(false) }
    var yearExpanded = remember { mutableStateOf(false) }

    var hourExpanded = remember { mutableStateOf(false) }
    var minuteExpanded = remember { mutableStateOf(false) }
    var amPMexpanded = remember { mutableStateOf(false) }

    AlertDialog(
        title = {
            if (mode.value == "add") {
                Text(
                    text = "Add Reminder", modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            if (mode.value == "edit") {
                Text(
                    text = "Edit Reminder", modifier = Modifier.padding(20.dp),
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
                        time = TextFieldValue(String.format("%02d:%02d %s", hour.text.toInt(), minute.text.toInt(), amPM.text))
                        reminderVM.editReminderList(
                            reminderItem,
                            name.text,
                            year.text,
                            month.text,
                            day.text,
                            time.text
                        )
                    }
                    if (mode.value == "add") {
                        reminderVM.addReminderList(
                            name.text,
                            year.text,
                            month.text,
                            day.text,
                            time.text
                        )
                    }
                    mode.value = "closed"
                }
            ) {
                Text("Confirm")
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
                    text = "Description" ,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp),
                    color = Color.DarkGray,
                )
                TextField(
                    value = name, onValueChange = { newText ->
                        name = newText
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, top= 5.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Row {
                    Column() {
                        Text(
                            text = "Year" ,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            modifier = Modifier.width(150.dp).padding(horizontal = 4.dp, vertical = 0.dp),
                            color = Color.DarkGray,
                        )
                        Box {
                            TextButton(
                                onClick = { yearExpanded.value = !yearExpanded.value },
                                modifier = Modifier.width(150.dp)
                            ) {
                                var defaultYear = year.text
                                if (defaultYear == "") {
                                    defaultYear = java.time.LocalDate.now().year.toString()
                                }
                                Text(text = defaultYear, textAlign = TextAlign.Center)
                                year = TextFieldValue(defaultYear);
                            }

                            DropdownMenu(
                                expanded = yearExpanded.value,
                                onDismissRequest = { yearExpanded.value = !yearExpanded.value },
                            ) {
                                for (i in 2010..2040) {
                                    DropdownMenuItem(
                                        onClick = {
                                            year = TextFieldValue(i.toString());
                                            yearExpanded.value = false
                                        },
                                        modifier = Modifier.width(150.dp)
                                    ) {
                                        Text(i.toString())
                                    }
                                }
                            }
                        }
                    }

                    Column() {
                        Text(
                            text = "Month" ,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            modifier = Modifier.width(150.dp).padding(horizontal = 4.dp, vertical = 0.dp),
                            color = Color.DarkGray,
                        )
                        Box {
                            TextButton(
                                onClick = { monthExpanded.value = !monthExpanded.value },
                                modifier = Modifier.width(150.dp)
                            ) {
                                var defaultMonth = month.text
                                if (defaultMonth == "") {
                                    defaultMonth = java.time.LocalDate.now().monthValue.toString()
                                }
                                Text(text = defaultMonth, textAlign = TextAlign.Center)
                                month = TextFieldValue(defaultMonth);
                            }

                            DropdownMenu(
                                expanded = monthExpanded.value,
                                onDismissRequest = { monthExpanded.value = !monthExpanded.value },
                            ) {
                                for (i in 1..12) {
                                    DropdownMenuItem(
                                        onClick = {
                                            month = TextFieldValue(i.toString());
                                            monthExpanded.value = false
                                        },
                                        modifier = Modifier.width(150.dp)
                                    ) {
                                        Text(i.toString())
                                    }
                                }
                            }
                        }
                    }

                    Column() {
                        Text(
                            text = "Day" ,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            modifier = Modifier.width(150.dp).padding(horizontal = 4.dp, vertical = 0.dp),
                            color = Color.DarkGray,
                        )
                        Box {
                            TextButton(
                                onClick = { dayExpanded.value = !dayExpanded.value },
                                modifier = Modifier.width(150.dp)
                            ) {
                                var defaultDay = day.text
                                if (defaultDay == "") {
                                    defaultDay = java.time.LocalDate.now().dayOfMonth.toString()
                                }
                                Text(text = defaultDay, textAlign = TextAlign.Center)
                                day = TextFieldValue(defaultDay);
                            }

                            DropdownMenu(
                                expanded = dayExpanded.value,
                                onDismissRequest = { dayExpanded.value = !dayExpanded.value },
                            ) {
                                var maxday : Int;
                                if (month.text == "2") {
                                    maxday = 28
                                    day = TextFieldValue("28")
                                } else if (month.text == "4" || month.text == "6" || month.text == "9" || month.text == "11") {
                                    maxday = 30
                                    day = TextFieldValue("30")
                                } else {
                                    maxday = 31
                                }
                                for (i in 1..maxday) {
                                    DropdownMenuItem(
                                        onClick = {
                                            day = TextFieldValue(i.toString());
                                            dayExpanded.value = false
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
                    Column() {
                        Text(
                            text = "Hour" ,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            modifier = Modifier.width(150.dp).padding(horizontal = 4.dp, vertical = 0.dp),
                            color = Color.DarkGray,
                        )
                        Box {
                            TextButton(
                                onClick = { hourExpanded.value = !hourExpanded.value },
                                modifier = Modifier.width(150.dp)
                            ) {
                                Text(text = hour.text, textAlign = TextAlign.Center)
                                time = TextFieldValue(String.format("%d%02d %s", hour.text.toInt(), minute.text.toInt(), amPM.text))
                            }

                            DropdownMenu(
                                expanded = hourExpanded.value,
                                onDismissRequest = { hourExpanded.value = !hourExpanded.value },
                            ) {
                                for (i in 1..12) {
                                    DropdownMenuItem(
                                        onClick = {
                                            hour = TextFieldValue(i.toString());
                                            time = TextFieldValue(String.format("%d:%02d %s", hour.text.toInt(), minute.text.toInt(), amPM.text))
                                            hourExpanded.value = false
                                        },
                                        modifier = Modifier.width(150.dp)
                                    ) {
                                        Text(i.toString())
                                    }
                                }
                            }
                        }
                    }

                    Column() {
                        Text(
                            text = "Minute" ,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            modifier = Modifier.width(150.dp).padding(horizontal = 4.dp, vertical = 0.dp),
                            color = Color.DarkGray,
                        )
                        Box {
                            TextButton(
                                onClick = { minuteExpanded.value = !minuteExpanded.value },
                                modifier = Modifier.width(150.dp)
                            ) {
                                Text(text = minute.text, textAlign = TextAlign.Center)
                                time = TextFieldValue(hour.text + ":" + minute.text + " " + amPM.text)
                            }

                            DropdownMenu(
                                expanded = minuteExpanded.value,
                                onDismissRequest = { minuteExpanded.value = !minuteExpanded.value },
                            ) {
                                for (i in 0..59) {
                                    DropdownMenuItem(
                                        onClick = {
                                            minute = TextFieldValue(i.toString())
                                            time = TextFieldValue(String.format("%d:%02d %s", hour.text.toInt(), minute.text.toInt(), amPM.text))
                                            minuteExpanded.value = false
                                        },
                                        modifier = Modifier.width(150.dp)
                                    ) {
                                        Text(i.toString())
                                    }
                                }
                            }
                        }
                    }

                    Column() {
                        Text(
                            text = "AM/PM" ,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            modifier = Modifier.width(150.dp).padding(horizontal = 4.dp, vertical = 0.dp),
                            color = Color.DarkGray,
                        )
                        Box {
                            TextButton(
                                onClick = { amPMexpanded.value = !amPMexpanded.value },
                                modifier = Modifier.width(150.dp)
                            ) {
                                Text(text = amPM.text, textAlign = TextAlign.Center)
                                time = TextFieldValue(String.format("%d:%02d %s", hour.text.toInt(), minute.text.toInt(), amPM.text))
                            }

                            DropdownMenu(
                                expanded = amPMexpanded.value,
                                onDismissRequest = { amPMexpanded.value = !amPMexpanded.value },
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        amPM = TextFieldValue("a.m.")
                                        time = TextFieldValue(String.format("%d:%02d %s", hour.text.toInt(), minute.text.toInt(), amPM.text))
                                        amPMexpanded.value = false
                                    },
                                    modifier = Modifier.width(150.dp)
                                ) {
                                    Text("a.m.")
                                }
                                DropdownMenuItem(
                                    onClick = {
                                        amPM = TextFieldValue("p.m.")
                                        time = TextFieldValue(String.format("%d:%02d %s", hour.text.toInt(), minute.text.toInt(), amPM.text))
                                        amPMexpanded.value = false
                                    },
                                    modifier = Modifier.width(150.dp)
                                ) {
                                    Text("p.m.")
                                }
                            }
                        }
                    }
                }
            }
        },
    )
}