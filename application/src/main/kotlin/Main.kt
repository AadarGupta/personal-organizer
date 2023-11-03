import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import files.FileModel
import files.FileViewModel
import files.ListFiles
import sidebar.SidebarContainer

// =================== HOMEPAGE SECTIONS ===================

@Composable
fun WelcomePage() {

    var fileLevel = remember { mutableStateOf("root") }

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
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 0.dp),
                color = Color.Black,
            )

        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            ListFiles(fileLevel)

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
                .fillMaxWidth(1f)
                .fillMaxHeight(1f)
        ) {
            WelcomePage()
        }

        Box(
            modifier = Modifier
                .background(androidx.compose.ui.graphics.Color.Gray)
                .width(400.dp)
                .fillMaxHeight(1f)
        ) {
            SidebarContainer()
        }
    }
}

fun clearDatabase() {
    transaction {
        FileDataObject.deleteAll();
        ReminderDataObject.deleteAll();
        ToDoDataObject.deleteAll();
    }
}

fun resetDatabase() {
    clearDatabase()
    resetDefaultFiles()
    resetDefaultReminders()
    resetDefaultToDos()
}

fun main() = application {

    Database.connect("jdbc:sqlite:personal-organizer.db")

    transaction {
        // create schemas
        SchemaUtils.create (FileDataObject)
        SchemaUtils.create (ReminderDataObject)
        SchemaUtils.create (ToDoDataObject)
    }

    // UNCOMMENT THE COMMAND BELOW TO RESET YOUR DB FILE TO DEFAULTS
    resetDatabase();

    Window(
        onCloseRequest = ::exitApplication,
        title="Personal Organizer",
        state = WindowState(
            width = 1000.dp, height = 700.dp,
            position = WindowPosition(50.dp, 50.dp)
        )
    ) {
        App()
    }

}
