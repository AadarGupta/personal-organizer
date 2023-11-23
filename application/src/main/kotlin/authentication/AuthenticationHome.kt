package authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthenticationPage(
    userAuthToken: MutableState<String>
) {

    var loginPopup = remember { mutableStateOf("closed") }
    var signupPopup = remember { mutableStateOf("closed") }


    if (loginPopup.value == "open") {
        LoginDialog(mode = loginPopup, token = userAuthToken)
    }
    if (signupPopup.value == "open") {
        SignupDialog(mode = signupPopup)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            Image(
                painter = painterResource("mypoLogo.png"),
                contentDescription = "app logo",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "My Personal Organizer",
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(start = 100.dp, top = 30.dp),
                color = Color.DarkGray,
            )

        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            Button(
                onClick = {
                    // check if credentials are valid
                    loginPopup.value = "open"
                }
            ) {
                Text("Login")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            Button(
                onClick = {
                    // check if credentials are valid
                    signupPopup.value = "open"
                }
            ) {
                Text("Register")
            }
        }
    }
}