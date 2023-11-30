

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import authentication.AuthenticationPage
import components.LogoHeader
import files.FileListContainer
import sidebar.SidebarContainer

// =================== HOMEPAGE SECTIONS ===================

@Composable
fun WelcomePage(
    currUser: MutableState<String>
) {

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        LogoHeader()

        Button(
            onClick = {
                // check if credentials are valid
                currUser.value = ""
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF67c2b3))
        ) {
            Text(text = "Logout", color = Color.White)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            FileListContainer(currUser)
        }
    }
}



@Composable
@Preview
fun App(
    currUser: MutableState<String>
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            modifier = Modifier
                .background(Color.White)
                .weight(1f)
                .fillMaxWidth(1f)
                .fillMaxHeight(1f)
                .padding(horizontal = 30.dp)
        ) {
            WelcomePage(currUser)
        }
        var sidebarVisible by remember { mutableStateOf(true) }
        Button(onClick = {sidebarVisible = !sidebarVisible},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF67c2b3)),
            shape = RoundedCornerShape(10.dp, 0.dp, 0.dp, 10.dp)
        ) {
            if (sidebarVisible) {
                Icon(
                    imageVector = Icons.Filled.Close, contentDescription = "Close",
                    tint = Color.White
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Menu, contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
        AnimatedVisibility(sidebarVisible) {
            val backgroundBrush = Brush.verticalGradient(listOf(Color(0xFF67c2b3), Color(0xFF7fe26d)))
            Box(
                modifier = Modifier
                    .background(backgroundBrush)
                    .width(400.dp)
                    .fillMaxHeight(1f)
            ) {
                SidebarContainer(currUser)
            }
        }
    }
}

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title="MyPO",
        icon = painterResource("mypoLogo.png"),
        state = WindowState(
            width = 1200.dp, height = 800.dp,
            position = WindowPosition(50.dp, 50.dp)
        )
    ) {
        var currUser = remember { mutableStateOf("") }

        if (currUser.value != "") {
            App(currUser)
        } else {
            AuthenticationPage(currUser)
        }
    }

}
