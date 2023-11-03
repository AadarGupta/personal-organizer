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
                .fillMaxWidth(0.70f)
                .fillMaxHeight(1f)
        ) {
            WelcomePage()
        }

        Box(
            modifier = Modifier
                .background(androidx.compose.ui.graphics.Color.Gray)
                .fillMaxWidth(0.30f)
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

fun resetDefaultToDos() {
    transaction {
        ToDoDataObject.insert {
            it[itemName] = "Get groceries"
        }
        ToDoDataObject.insert {
            it[itemName] = "Plan a heist"
        }
        ToDoDataObject.insert {
            it[itemName] = "Learn how to walk"
        }
        ToDoDataObject.insert {
            it[itemName] = "Hack NASA"
        }
    }
}

fun resetDefaultReminders() {
    transaction {

        ReminderDataObject.insert {
            it[itemName] = "Pay hydro"
        }
        ReminderDataObject.insert {
            it[itemName] = "Pay rent"
        }
        ReminderDataObject.insert {
            it[itemName] = "Go to bed earlier"
        }
    }
}

fun resetDefaultFiles() {
    transaction {

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "root"
            it[fileName] = "File 1"
            it[fileContent] = "File Content 1"
        }

        FileDataObject.insert {
            it[isFolder] = true
            it[parent] = "root"
            it[fileName] = "Folder 1"
            it[fileContent] = ""
        }

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "Folder 1"
            it[fileName] = "File 1.1"
            it[fileContent] = "File Content 1.1"
        }

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "Folder 1"
            it[fileName] = "File 1.2"
            it[fileContent] = "File Content 1.2"
        }

        FileDataObject.insert {
            it[isFolder] = true
            it[parent] = "Folder 1"
            it[fileName] = "Folder 1.1"
            it[fileContent] = ""
        }

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "Folder 1.1"
            it[fileName] = "File 1.1.1"
            it[fileContent] = "File Content 1.1.1"
        }

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "root"
            it[fileName] = "File 2"
            it[fileContent] = "File Content 2"
        }

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "root"
            it[fileName] = "File 3"
            it[fileContent] = "File Content 3"
        }
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
    //resetDatabase();

    Window(onCloseRequest = ::exitApplication, title="Personal Organizer") {
        App()
    }

}
