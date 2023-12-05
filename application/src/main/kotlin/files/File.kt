package files

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// List of Files (on homepage)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileListContainer(currUser: MutableState<String>) {
    // Initializes the FileViewModel for the current user and initializes default values (start menu)
    var fileVM = FileViewModel(currUser)
    var selectedItemIdx = remember { mutableStateOf(-1) }
    val dialogMode = remember { mutableStateOf("closed") }
    val dialogType = remember { mutableStateOf("none") }
    val parentLevel = remember { mutableStateOf(-1) }

    // If a new folder/file is being created or its name is edited, open the name dialog
    if (dialogMode.value == "add" || dialogMode.value == "edit") {
        FileDialog(dialogMode, dialogType, parentLevel, selectedItemIdx.value, fileVM)
    }

    // If a new folder/file is being previewed, open the preview window
    if(dialogMode.value == "preview") {
        FilePreview(dialogMode, selectedItemIdx.value, fileVM)
    }

    // If a file is being moved, open the move dialog
    if(dialogMode.value == "move") {
        MoveDialog(dialogMode, parentLevel, selectedItemIdx.value, fileVM)
    }

    // UI for the list of files
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row()
            {
                // Heading for the files and folders with a tooltip explaining what to do
                Text(
                    text = "Files & Folders",
                    fontWeight = FontWeight.Light,
                    color = Color.DarkGray,
                    fontSize = 20.sp
                )
                TooltipArea(
                    tooltip = {
                        // composable tooltip content
                        Surface(

                            color = Color.Gray.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Create, edit, delete and move markdown files and folders.",
                                modifier = Modifier.padding(10.dp), color = Color.Black
                            )
                        }
                    },
                    modifier = Modifier.padding(start = 10.dp),
                    delayMillis = 600, // delay in milliseconds
                    tooltipPlacement = TooltipPlacement.CursorPoint(
                        alignment = Alignment.BottomEnd
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info, contentDescription = "Todo Tooltip",
                        tint = Color.Black.copy(alpha = 0.5f)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                // Buttons for new file and new folder options
                Button(
                    onClick = {
                        // If a new file needs to be updated, updated mode and type of dialog
                        dialogMode.value = "add"
                        dialogType.value = "file"
                    },
                    modifier = Modifier.height(40.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF67c2b3)))
                {
                    Icon(
                        imageVector = Icons.Filled.Add, contentDescription = "Create New File Icon",
                        tint = Color.White
                    )
                    Text(
                        text = "New File",
                        fontSize = 12.sp,
                        color = Color.White,
                    )
                }

                Button(
                    onClick = {
                        // If a new folder needs to be updated, updated mode and type of dialog
                        dialogMode.value = "add"
                        dialogType.value = "folder"
                    },
                    modifier = Modifier.height(40.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF67c2b3)))
                {
                    Icon(
                        imageVector = Icons.Filled.Add, contentDescription = "Create New Folder Icon",
                        tint = Color.White
                    )
                    Text(
                        text = "New Folder",
                        fontSize = 12.sp,
                        color = Color.White,
                    )
                }
            }

        }

        // If the view model is empty (no files), prompt user to create one
        if (fileVM.isEmpty()) {
            Text(
                text = "No files found. Create one with the button above!", textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxHeight()
            )
        } else {
            // Check if the user is not at the root
            if (parentLevel.value != -1) {
                // Show a button to go back to the previous level
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                        .clickable {
                            // Switch the parent level to the current parent's parent
                            parentLevel.value = fileVM.getFileById(parentLevel.value).parent
                        }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = "Back",
                    )
                    Text(
                        text = "Go Back",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                    )
                }
            }
            // Row to show paramters for each file
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(start = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    // Type header (file or folder)
                    Text(
                        text = "Type",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(vertical = 2.dp),
                        color = Color.Gray,
                    )
                    // Filename header
                    Text(
                        text = "Name",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(vertical = 2.dp),
                        color = Color.Gray,
                    )
                }

                // Header for actions
                Text(
                    text = "Actions",
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, end = 25.dp),
                    color = Color.Gray,
                )
            }

            // LazyColumn to load each file, dependent on parent level
            LazyColumn {
                items(fileVM.getFileList(parentLevel.value)) {
                    // Creates a card for each file/folder
                    Card(
                        modifier = Modifier
                            .clickable {
                                // If the clicked file is a folder, open it as a parent folder
                                selectedItemIdx.value = fileVM.getFileIdx(it)
                                if (it.isFolder) {
                                    parentLevel.value = it.id
                                }
                                // If it is a file, open it as a preview
                                else {
                                    dialogMode.value = "preview"
                                }
                            }
                            .padding(vertical = 5.dp)
                    ) {
                        // Shows the UI for the file/folder card
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().height(30.dp)
                            ) {
                                // Choose an appropriate icon => file or folder
                                if (it.isFolder) {
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
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Show the name of the file
                                    Text(
                                        text = it.fileName,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp)
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                        // Button to edit the name of the title
                                        Icon(
                                            imageVector = Icons.Filled.Edit, contentDescription = "Edit",

                                            modifier = Modifier.clickable {
                                                selectedItemIdx.value = fileVM.getFileIdx(it)
                                                dialogMode.value = "edit"
                                            },
                                            tint = Color.DarkGray
                                        )

                                        // Button to move the file into a folder
                                        Icon(
                                            imageVector = Icons.Filled.Send, contentDescription = "Move",

                                            modifier = Modifier.clickable {
                                                // Open move dialog and move item to selected folder
                                                selectedItemIdx.value = fileVM.getFileIdx(it)
                                                dialogMode.value = "move"
                                            },
                                            tint = Color(0xFF7fe26d)
                                        )

                                        // Button to delete the file
                                        Icon(
                                            imageVector = Icons.Filled.Delete, contentDescription = "Delete",

                                            modifier = Modifier.clickable {
                                                fileVM.removeFileItem(it);
                                            },
                                            tint = Color(0xFF67c2b3)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


