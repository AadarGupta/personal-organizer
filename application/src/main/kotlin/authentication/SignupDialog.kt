package authentication

import MyHttp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignupDialog(
    mode: MutableState<String>
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameInUse by remember { mutableStateOf(false) }

    AlertDialog(
        title = {
            Text(
                text = "Login",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )

        },
        onDismissRequest = { mode.value = "closed" },
        confirmButton = {
            TextButton(
                onClick = {
                    val http = MyHttp()
                    val body = JsonObject(
                        mapOf(
                            "username" to JsonPrimitive(username),
                            "password" to JsonPrimitive(password)
                        )
                    )

                    val createUserResponse = http.post("user/signup", body)
                    if (createUserResponse.statusCode() == 200) {
                        mode.value = "closed"
                    } else {
                        usernameInUse = true
                    }
                }
            ) {
                Text("Register")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { mode.value = "closed" }
            ) {
                Text("Dismiss")
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(10.dp),
            ) {

                TextField(
                    value = username, onValueChange = { newText ->
                        username = newText
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                TextField(
                    value = password, onValueChange = { newText ->
                        password = newText
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                if (usernameInUse) {
                    Text(
                        text = "Username already in use",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }

        },
    )
}