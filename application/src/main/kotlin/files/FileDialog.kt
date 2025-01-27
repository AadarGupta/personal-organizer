package files

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// File Dialog (to rename or create a file)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FileDialog(
    mode: MutableState<String>,
    type: MutableState<String>,
    parentLevel: MutableState<Int>,
    fileItemIdx: Int,
    fileVM: FileViewModel
) {
    // Creates a default file item
    var fileItem = FileModel(-1  , fileVM.currUser.value, false, -1, "", "")
    // If a file is to be edited, get the file (in context) from the view model
    if (mode.value == "edit") {
        fileItem = fileVM.getFileByIdx(fileItemIdx)
    }
    // Converts the name to a TextFieldValue
    var item by remember { mutableStateOf(TextFieldValue(fileItem.fileName)) }

    // Creates an alert dialog with an appropriate title
    AlertDialog(
        title = {
            var dialogTitle = ""
            if (mode.value == "add") {
                if (type.value == "folder") {
                    dialogTitle = "Add Folder"
                }
                if (type.value == "file") {
                    dialogTitle = "Add File"
                }
            }
            if (mode.value == "edit") {
                if (type.value == "folder") {
                    dialogTitle = "Edit Folder"
                }
                if (type.value == "file") {
                    dialogTitle = "Edit File"
                }
            }
            // Generates the title as text
            Text(
                text = dialogTitle, modifier = Modifier.padding(20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        onDismissRequest = { mode.value = "closed" },
        confirmButton = {
            // Confirm button makes appropriate edits/creations
            TextButton(
                onClick = {
                    // If the file needs to be edited, the file in the view model is changed
                    if (mode.value == "edit") {
                        fileVM.editFileName(fileItem, item.text)
                    }
                    // If a new file needs to be created, a folder/file is created in view model
                    if (mode.value == "add") {
                        if (type.value == "folder") {
                            fileVM.addFile(true, parentLevel.value, item.text)
                        } else if (type.value == "file") {
                            fileVM.addFile(false, parentLevel.value, item.text)
                        }
                    }
                    // Closes the dialog
                    mode.value = "closed"
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            // Closes the dialog without any changes or creations
            TextButton(
                onClick = { mode.value = "closed" }
            ) {
                Text("Dismiss")
            }
        },
        text = {
            // Creates a TextField area for the file name changes
            Column(
                modifier = Modifier.padding(10.dp),
            ) {

                TextField(
                    value = item, onValueChange = { newText ->
                        item = newText
                    },
                    maxLines = 1,
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