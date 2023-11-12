

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
import files.FileListContainer
import sidebar.SidebarContainer

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
            FileListContainer()
            //ListFiles(fileLevel)

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

fun main() = application {

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
