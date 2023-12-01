package authentication

import MyHttp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    AlertDialog(
        title = {
            Text(
                text = "Signup",
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

                    runBlocking {
                        launch {
                            val createUserResponse = http.post("user/signup", body)
                            if (createUserResponse.status == HttpStatusCode.OK) {
                                mode.value = "closed"
                            } else {
                                usernameInUse = true
                            }
                        }
                    }
                }
            ) {
                Text("Signup")
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

                Column(modifier = Modifier.padding(5.dp)) {
                    Text("Username")
                    TextField(
                        value = username, onValueChange = { newText ->
                            username = newText
                        },
                        maxLines = 1,
                        placeholder = { Text("Enter username") },
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
                }

                Column(modifier = Modifier.padding(5.dp)) {
                    Text("Password")
                    TextField(
                        value = password, onValueChange = { newText ->
                            password = newText
                        },
                        maxLines = 1,
                        placeholder = { Text("Enter password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val passwordVisibilityImage = if (passwordVisibility)
                                "hidePassword.png"
                            else "showPassword.png"

                            // Please provide localized description for accessibility services
                            val description = if (passwordVisibility) "Hide password" else "Show password"

                            IconButton(
                                onClick = {passwordVisibility = !passwordVisibility},
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .width(20.dp)
                            ){
                                Image(
                                    painter = painterResource(passwordVisibilityImage),
                                    contentDescription = description
                                )
                            }
                        }
                    )
                    if (usernameInUse) {
                        Text(
                            text = "Username already in use",
                            color = Color.Red,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            }

        },
    )
}