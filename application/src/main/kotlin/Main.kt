

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import files.ListFiles
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
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
                text = "Personal Organizer",
                fontSize = 40.sp,
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 20.dp),
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
                .padding(horizontal = 30.dp)
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
    }

    // UNCOMMENT THE COMMAND BELOW TO RESET YOUR DB FILE TO DEFAULTS
    resetDatabase();

    Window(
        onCloseRequest = ::exitApplication,
        title="Personal Organizer",
        state = WindowState(
            width = 1200.dp, height = 800.dp,
            position = WindowPosition(50.dp, 50.dp)
        )
    ) {
        App()
    }

}
