package authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LogoHeader()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    // check if credentials are valid
                    currUser.value = "demo-user"
                }
            ) {
                Text("Login as Demo User")
            }
        }
    }
}