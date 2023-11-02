package sidebar.todos

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ToDoContainer() {

    var toDoVM = ToDoViewModel()

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .border( width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(10.dp)
                )

        ) {
            Text(
                text = "To-Dos",
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                color = Color.White,
            )
        }

        if (toDoVM.isToDoEmpty()) {
            Text(
                text = "No to do items.", textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxHeight()
            )
        } else {
            LazyColumn {
                items(toDoVM.getToDoList()) {
                    Card(
                        backgroundColor = Color.Gray,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {  }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Checkbox(
                                checked = it.isChecked,
                                onCheckedChange = { value -> toDoVM.checkToDoItem(it, value)}
                            )
                            Text(
                                text = it.itemName,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                                color = Color.White,
                            )
                            Icon(
                                imageVector = Icons.Filled.Delete, contentDescription = "Delete",

                                modifier = Modifier.clickable {
                                    toDoVM.removeToDoItem(it);
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