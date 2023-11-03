package files

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window


class File(filename: String) {
    var isFolder = false
    var parent = "root"
    var filename = filename
    var content = ""
}


@Composable
fun FileListItem(file: File, fileLevel: MutableState<String>) {

    val fileClicked = remember { mutableStateOf(false) }
    if (fileClicked.value) {
        if (file.isFolder) {
            // open folder view
            fileLevel.value = file.filename;
            fileClicked.value = false; // needed to prevent double click
        } else {
            // open editor
            FileEdit(file, fileClicked);
        }
    }

    TextButton(
        onClick = { fileClicked.value = !fileClicked.value },
        modifier = Modifier.height(50.dp)
    ) {
        if (file.isFolder) {
            Image(
                painter = painterResource("folderIcon.png"),
                contentDescription = "Folder Icon"
            )
        } else {
            Image(
                painter = painterResource("fileIcon.png"),
                contentDescription = "File Icon"
            )
        }
        Text(
            text = file.filename,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 50.dp, vertical = 0.dp),
            color = Color.Black,
        )
    }
}


@Composable
fun FileEdit(file: File, showEditView: MutableState<Boolean>) {
    var isAskingToClose = remember { mutableStateOf(true) }
    if (isAskingToClose.value) {
        Window(
            onCloseRequest = {
                isAskingToClose.value = !isAskingToClose.value
                showEditView.value = !showEditView.value
            }
        ) {
            var query = remember { mutableStateOf(file.content) }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                ) {
                    Text(
                        text = file.filename,
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
                        BasicTextField(
                            value = query.value,
                            onValueChange = { query.value = it },
                            modifier = Modifier.padding(horizontal = 15.dp, vertical = 0.dp),
                        )
                    }
                }

            }
        }
    }
}


