package files

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


@Composable
fun FileListItem(file: FileModel, fileLevel: MutableState<String>, fileList: FileViewModel) {

    val fileClicked = remember { mutableStateOf(false) }

    if (fileClicked.value) {
        if (file.isFolder) {
            // open folder view
            fileLevel.value = file.fileName;
            fileClicked.value = false; // needed to prevent double click
        } else {
            // open editor
            FileEdit(file, fileClicked, fileList);
        }
    }

    Row(modifier = Modifier.height(50.dp)) {


        TextButton(
            onClick = { fileClicked.value = !fileClicked.value },

        ) {
            val showFileEdit = remember { mutableStateOf(false) }
            if (showFileEdit.value) {
                FileEdit(file, showFileEdit, fileList)
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
                text = file.fileName,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Icon(
                imageVector = Icons.Filled.Delete, contentDescription = "Delete",

                modifier = Modifier.clickable {
                    fileList.removeFileItem(file)
                },
                tint = Color.Red
            )


        }
    }

}

@Composable
fun ListFiles(fileLevel: MutableState<String>) {

    var toCreate = remember { mutableStateOf(false) }

    var fileList = FileViewModel();

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        Row( modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp),) {
            TextButton(onClick = { toCreate.value = !toCreate.value }, modifier = Modifier.height(50.dp)) {
                if(toCreate.value) {
                    FileCreate(toCreate, fileList)
                }
                Image(
                    painter = painterResource("createFileIcon.png"),
                    contentDescription = "Create New File Icon",
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = "Create New File",
                    fontSize = 15.sp,
                    color = Color.Blue,
                )
            }

            TextButton(onClick = { toCreate.value = !toCreate.value }, modifier = Modifier.height(50.dp)) {
                if(toCreate.value) {
                    FolderCreate(toCreate, fileList, fileLevel.value)
                }
                Image(
                    painter = painterResource("newFolderIcon.png"),
                    contentDescription = "Create New Folder Icon",
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = "Create New Folder",
                    fontSize = 15.sp,
                    color = Color.Blue,
                )
            }


        }

        if (fileLevel.value != "root") {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                TextButton(
                    onClick = {
                        // get parents filename
                        transaction {
                            for (file in FileDataObject.selectAll()) {
                                if (file[FileDataObject.fileName] == fileLevel.value) {
                                    fileLevel.value = file[FileDataObject.parent]
                                }
                            }
                        }
                    },
                    modifier = Modifier.height(50.dp)
                ) {
                    // add back arrow here
                    Text(
                        text = "..",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 50.dp, vertical = 0.dp),
                        color = Color.Black,
                    )
                }
            }
        }

            for (targetFile in fileList.getFileList()) {
                if (targetFile.parent == fileLevel.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                    ) {
                        FileListItem(targetFile, fileLevel, fileList)
                    }
                }

            }
    }
}


@Composable
fun FileEdit(file: FileModel, showEditView: MutableState<Boolean>, fileList: FileViewModel) {
    var isAskingToClose = remember { mutableStateOf(true) }

    if (isAskingToClose.value) {
        Window(
            onCloseRequest = {
                isAskingToClose.value = !isAskingToClose.value
                showEditView.value = !showEditView.value
            }, title="Editing: ${file.fileName}"
        ) {
            var query = remember { mutableStateOf(file.fileContent) }
            // Add save button to handle editing
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                ) {
                    Text(
                        text = file.fileName,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                        color = Color.Black,
                    )

                    Icon(
                            imageVector = Icons.Filled.Done, contentDescription = "Save",
                            modifier = Modifier.clickable {
                                fileList.editFileList(file, query.value)
                                }.height(40.dp),
                            tint = Color.Blue
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FileCreate(active: MutableState<Boolean>, fileList: FileViewModel) {
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

                    // Need to figure this out
                    fileList.addFileList(false, "root", item.text, "")
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FolderCreate(active: MutableState<Boolean>, fileList: FileViewModel, parentFolder: String) {
    var item by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        title = {
            Text(
                text = "Create New Folder", modifier = Modifier.padding(20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        onDismissRequest = { active.value = false },
        confirmButton = {
            TextButton(
                onClick = {

                    // Need to figure this out
                    fileList.addFileList(true, parentFolder, item.text, "")
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


