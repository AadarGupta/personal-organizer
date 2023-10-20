package files

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

class File(filename: String) {
    var filename = filename
    var content = ""
}


@Composable
fun FileListItem(file: File) {
    TextButton(
        onClick = { }
    ) {
        Text(
            text = file.filename,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 50.dp, vertical = 0.dp),
            color = Color.Black,
        )
    }
}


@Composable
fun FileEdit(fileName: String, fileContent: String) {
    var query = remember { mutableStateOf(fileContent) }
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            Text(
                text = fileName,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                color = androidx.compose.ui.graphics.Color.Black,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                BasicTextField(value=query.value, onValueChange = {query.value = it},modifier = Modifier.padding(horizontal = 15.dp, vertical = 0.dp),
                )
            }
        }

    }
}


fun openTextWindow(file: File) {
    /*Window(onCloseRequest = ::exitApplication, title="Editing $file.filename") {
        FileEdit(file.filename, file.content)
    }*/
}


