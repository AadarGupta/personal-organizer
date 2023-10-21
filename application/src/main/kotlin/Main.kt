import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import files.File
import files.FileEdit
import files.FileItemList
import sidebar.SidebarContainer

// =================== HOMEPAGE SECTIONS ===================

@Composable
fun WelcomePage() {
    var file1 = File("file1")
    file1.content = "Contents of file1"
    var file2 = File("file2")
    file2.content = "Contents of file2"
    var file3 = File("file3")
    file3.content = "Contents of file3"

    var fileList = listOf(file1, file2, file3)

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
            FileItemList(fileList)
        }
    }
}


@Composable
fun ViewHandler(editView: MutableState<Boolean>) {
    Switch(
        checked = editView.value,
        onCheckedChange = { editView.value = it }
    )
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
            // For demo only: WelcomePage is Home, FileEdit is sample editing page
            //WelcomePage()
            FileEdit("CS 346", "Welcome to CS 346! In this course you will form four-person project teams and work together to design, develop and test a robust full-stack application.\n" +
                    "\n" +
                    "Modern software is often too complex for a single person to design and build on their own. By working together, we can pool everyone’s talents to tackle much larger, more complex projects. Our goal is to use best-practices to design and build a commercial-quality, robust, full-featured application.")
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

fun main() = application {

    Window(onCloseRequest = ::exitApplication, title="Personal Organizer") {
        App()
    }

    /*
    Window(onCloseRequest = ::exitApplication, title="Editing file") {
        FileEdit("asdf1234", "asdf")
    }
    */


}
