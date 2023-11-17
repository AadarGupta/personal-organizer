package files

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun FileListContainer() {
    var fileVM = FileViewModel()
    var selectedItemIdx = remember { mutableStateOf(-1) }
    val dialogMode = remember { mutableStateOf("closed") }
    val dialogType = remember { mutableStateOf("none") }
    val parentLevel = remember { mutableStateOf(-1) }

    if (dialogMode.value == "add" || dialogMode.value == "edit") {
        FileDialog(dialogMode, dialogType, parentLevel, selectedItemIdx.value, fileVM)
    }
    if(dialogMode.value == "preview") {
        FilePreview(dialogMode, selectedItemIdx.value, fileVM)
    }

    if(dialogMode.value == "move") {
        MoveDialog(dialogMode, parentLevel, selectedItemIdx.value, fileVM)
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Files & Folders",
                fontWeight = FontWeight.Light,
                color = Color.DarkGray,
                fontSize = 20.sp
            )
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Button(
                    onClick = {
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
                        dialogMode.value = "add"
                        dialogType.value = "folder"
                    },
                    modifier = Modifier.height(40.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF67c2b3)))
                {
                    Icon(
                        imageVector = Icons.Filled.Add, contentDescription = "Create New File Icon",
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

        if (fileVM.isEmpty()) {
            Text(
                text = "No files found. Create one with the button above!", textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxHeight()
            )
        } else {
            if (parentLevel.value != -1) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                        .clickable {
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(start = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    Text(
                        text = "Type",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(vertical = 2.dp),
                        color = Color.Gray,
                    )
                    Text(
                        text = "Name",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(vertical = 2.dp),
                        color = Color.Gray,
                    )
                }

                Text(
                    text = "Actions",
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, end = 25.dp),
                    color = Color.Gray,
                )
            }

            LazyColumn {
                items(fileVM.getFileList(parentLevel.value)) {
                    Card(
                        modifier = Modifier
                            .clickable {
                                selectedItemIdx.value = fileVM.getFileIdx(it)
                                if (it.isFolder) {
                                    parentLevel.value = it.id
                                } else {
                                    dialogMode.value = "preview"
                                }
                            }
                            .padding(vertical = 5.dp)
                    ) {
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
                                    Text(
                                        text = it.fileName,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp)
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                        Icon(
                                            imageVector = Icons.Filled.Edit, contentDescription = "Edit",

                                            modifier = Modifier.clickable {
                                                selectedItemIdx.value = fileVM.getFileIdx(it)
                                                dialogMode.value = "edit"
                                            },
                                            tint = Color.DarkGray
                                        )

                                        Icon(
                                            imageVector = Icons.Filled.Send, contentDescription = "Move",

                                            modifier = Modifier.clickable {
                                                // fileVM.moveItem(it);
                                                // Open move dialog and move item to selected folder
                                                selectedItemIdx.value = fileVM.getFileIdx(it)
                                                dialogMode.value = "move"
                                            },
                                            tint = Color(0xFF7fe26d)
                                        )

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


