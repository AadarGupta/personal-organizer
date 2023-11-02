package files

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue

class File(filename: String) {
    var isFolder = false
    var parent = "root"
    var filename = filename
    var content = ""
}


@Composable
fun FileListItem(file: File, fileLevel: MutableState<String>) {

    val fileClicked = remember { mutableStateOf(false) }
    var toDelete = remember { mutableStateOf(false) }

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

    Row(modifier = Modifier.height(50.dp)) {


        TextButton(
            onClick = { fileClicked.value = !fileClicked.value },

        ) {
            val showFileEdit = remember { mutableStateOf(false) }
            if (showFileEdit.value) {
                FileEdit(file, showFileEdit)
            }

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
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 4.dp)
            )


        }

        TextButton(onClick = { toDelete.value = !toDelete.value }
            ) {
            /*if(toCreate.value) {
            // Needs to be FileDelete which pulls a list of files and deletes the clicked ones
            FileCreate(toCreate)
        }*/

            Image(
                painter = painterResource("trashIcon.png"),
                contentDescription = "Delete File Icon",
                modifier = Modifier.height(30.dp)
            )
        }
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
            }, title="Editing: ${file.filename}"
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

// Function should create a new file
fun createNewFile(filename: String) {
    println("Creating a new file called '${filename}'...")
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FileCreate(
    active: MutableState<Boolean>
) {
    var item by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        title = {
            Text(
                text = "Create New File", modifier = Modifier.padding(20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        onDismissRequest = { active.value = false },
        confirmButton = {
            TextButton(
                onClick = {
                    //toDoVM.editToDoList(toDoItem, item.text)
                    active.value = false
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { active.value = false }
            ) {
                Text("Dismiss")
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(10.dp),
            ) {

                TextField(
                    value = item, onValueChange = { newText ->
                        item = newText
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
            }

        },
    )
}


