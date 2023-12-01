package files

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HelpMenu(
    mode: MutableState<String>) {

    val mappings = listOf(
        "#" to "Heading 1",
        "##" to "Heading 2",
        "###" to "Heading 3",
        "####" to "Heading 4",
        "#####" to "Heading 5",
        "######" to "Heading 6",
        "-" to "Point form",
        "`code`" to "Code Line",
        "<url>" to "Clickable Link (URL)",
        "!(url)" to "Image from URL"
    )

    AlertDialog(
        title = {
            Text(
                    text = "Help Menu for Markdown Commands", modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF67c2b3),
                )
        },
        onDismissRequest = { mode.value = "closed" },
        confirmButton = {
            TextButton(
                onClick = {
                    mode.value = "closed"
                }
            ) {
                Text("Close")
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(10.dp),
            ) {
                mappings.forEach { (symbol, description) ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = symbol,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 20.dp),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = description,
                            textAlign = TextAlign.Right,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 20.dp),
                            color = Color.Black
                        )
                    }
                }
            }

        },
    )
}