package authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import components.LogoHeader

@Composable
fun AuthenticationPage(
    currUser: MutableState<String>
) {

    var loginPopup = remember { mutableStateOf("closed") }
    var signupPopup = remember { mutableStateOf("closed") }


    if (loginPopup.value == "open") {
        LoginDialog(mode = loginPopup, currUser = currUser)
    }
    if (signupPopup.value == "open") {
        SignupDialog(mode = signupPopup)
    }

    LogoHeader()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    // check if credentials are valid
                    loginPopup.value = "open"
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF67c2b3))
            ) {
                Text(text = "Login", color = Color.White)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    // check if credentials are valid
                    signupPopup.value = "open"
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF67c2b3))
            ) {
                Text(text = "Register", color = Color.White)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    // check if credentials are valid
                    currUser.value = "demo-user"
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF67c2b3))
            ) {
                Text(text = "Login as Demo User", color = Color.White)
            }
        }
    }
}