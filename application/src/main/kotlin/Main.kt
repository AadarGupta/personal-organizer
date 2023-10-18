import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

// =================== BASIC ITEMS ===================
@Composable
fun FileItem(fileName: String) {
    Text(fileName)
}

@Composable
fun ToDoItem(toDoItemName: String) {
    Text(toDoItemName) // style this
}

@Composable
fun ReminderItem(reminderItemName: String) {
    Text(reminderItemName) // style this
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
            FileItem("file1")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            FileItem("file2")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            FileItem("file3")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            FileItem("file4")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            FileItem("file5")
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
        ) {
            Text("To-Dos")
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
        ) {
            Text("Reminders")
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
            Text("Welcome!") // style this
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
                .background(androidx.compose.ui.graphics.Color.White)
                .fillMaxWidth(1f)
        ) {
            Text("Tool Sidebar")
        }

        Box(
            modifier = Modifier
                .padding(10.dp)
                .border(2.dp, androidx.compose.ui.graphics.Color.Black)
                .background(androidx.compose.ui.graphics.Color.White)
                .height(200.dp)
                .fillMaxWidth(1f)
                .padding(5.dp)
        ) {
            ToDoContainer()
        }

        Box(
            modifier = Modifier
                .padding(10.dp)
                .border(2.dp, androidx.compose.ui.graphics.Color.Black)
                .background(androidx.compose.ui.graphics.Color.White)
                .height(200.dp)
                .fillMaxWidth(1f)
                .padding(5.dp)
        ) {
            RemindersContainer()
        }
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
                .fillMaxWidth(0.7f)
                .fillMaxHeight(1f)
        ) {
            WelcomePage()
        }

        Box(
            modifier = Modifier
                .background(androidx.compose.ui.graphics.Color.Gray)
                .fillMaxWidth(0.3f)
                .fillMaxHeight(1f)
        ) {
            Sidebar()
        }
    }

    /*
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
    */
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
