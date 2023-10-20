import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.TextButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

// =================== BASIC ITEMS ===================
// need to implement a file class
var name = "test_file1"
var content = "SKr SKr please Jeff give us good mark please JEff!!"

@Composable
fun FileItem(fileName: String, fileContent: String) {
    //Text(fileName)
    TextButton(onClick = {
        name = fileName
        content = fileContent
    }) {
        Text(
            text = fileName,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 50.dp, vertical = 0.dp),
            color = androidx.compose.ui.graphics.Color.Black,
        )    }
}

@Composable
fun ToDoItem(toDoItemName: String) {
    Text(
        text = toDoItemName,
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
        color = androidx.compose.ui.graphics.Color.White,
    )
}

@Composable
fun ReminderItem(reminderItemName: String) {
    Text(
        text = reminderItemName,
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
        color = androidx.compose.ui.graphics.Color.White,
    )
}

@Composable
fun FileList() {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            FileItem("file1", fileContent = "These are notes for course 1")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            FileItem("file2", fileContent = "These are notes for course 2")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            FileItem("file3", fileContent = "These are notes for course 3")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            FileItem("file4", fileContent = "These are notes for course 4")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            FileItem("file5", fileContent = "These are notes for course 5")
        }
    }
}

// =================== SIDEBAR SECTIONS ===================

@Composable
fun ToDoContainer() {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .border( width = 2.dp,
                        color = androidx.compose.ui.graphics.Color.Gray,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp))

        ) {
            Text(
                text = "To-Dos",
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                color = androidx.compose.ui.graphics.Color.White,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            ToDoItem("Brush teeth")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            ToDoItem("Shower")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            ToDoItem("Buy groceries")
        }
    }
}

@Composable
fun RemindersContainer() {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .border( width = 2.dp,
                    color = androidx.compose.ui.graphics.Color.Gray,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp))

        ) {
            Text(
                text = "Reminders",
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                color = androidx.compose.ui.graphics.Color.White,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            ReminderItem("Sprint 1")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            ReminderItem("Sprint 2")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            ReminderItem("Sprint 3")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            ReminderItem("Sprint 4")
        }
    }
}

// =================== HOMEPAGE SECTIONS ===================

@Composable
fun WelcomePage() {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            Text(
                text = "Hello Jeff!",
                fontSize = 40.sp,
                modifier = Modifier.padding(horizontal = 40.dp, vertical = 40.dp),
                color = androidx.compose.ui.graphics.Color.Black,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            FileList()
        }
    }
}


@Composable
fun Sidebar() {

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Box(
            modifier = Modifier
                .background(androidx.compose.ui.graphics.Color.Gray)
                .fillMaxWidth(1f)
                .height(75.dp)
        ) {
            Text(
                text = "Toolbar",
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                color = androidx.compose.ui.graphics.Color.White,
            )
        }

        Box(
            modifier = Modifier
                .padding(10.dp)
                .border( width = 2.dp,
                    color = androidx.compose.ui.graphics.Color.White,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                .background(androidx.compose.ui.graphics.Color.Gray)
                .height(200.dp)
                .fillMaxWidth(1f)
                .padding(5.dp)


        ) {
            ToDoContainer()
        }

        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(androidx.compose.ui.graphics.Color.Gray)
                .border( width = 2.dp,
                    color = androidx.compose.ui.graphics.Color.White,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                .height(200.dp)
                .fillMaxWidth(1f)
                .padding(5.dp)

        ) {
            RemindersContainer()
        }
    }
}

@Composable
fun TextPage(fileName: String, fileContent: String) {
    var query = remember { mutableStateOf(fileContent) }
    Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
                modifier = Modifier
                        .fillMaxWidth(1f)
        ) {
            Text(
                text = fileName,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                color = androidx.compose.ui.graphics.Color.Black,
            )
        }
        Row(
                modifier = Modifier
                        .fillMaxWidth(1f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                BasicTextField(value=query.value, onValueChange = {query.value = it},modifier = Modifier.padding(horizontal = 15.dp, vertical = 0.dp),
                )
            }
        }

    }
}

fun openTextWindow(fileName: String, fileContent: String) = application {
    Window(onCloseRequest = ::exitApplication, title="Editing $fileName") {
        TextPage(fileName, fileContent)
    }
}

@Composable
@Preview
fun App() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            modifier = Modifier
                .background(androidx.compose.ui.graphics.Color.White)
                .weight(1f)
                .fillMaxWidth(0.70f)
                .fillMaxHeight(1f)
        ) {
            // Need to figure out why this doesn't update
            WelcomePage()
//            TextPage(name, content)
        }

        Box(
            modifier = Modifier
                .background(androidx.compose.ui.graphics.Color.Gray)
                .fillMaxWidth(0.30f)
                .fillMaxHeight(1f)
        ) {
            Sidebar()
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title="CS346 Note-Taking App") {
        App()
    }
}
