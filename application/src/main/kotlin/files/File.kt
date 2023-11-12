package files

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    if (dialogMode.value == "editContent") {
        FileEditDialog(dialogMode, selectedItemIdx.value, fileVM)
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
                text = "My Files",
                fontSize = 25.sp
            )
            Row() {
                TextButton(
                    onClick = {
                        dialogMode.value = "add"
                        dialogType.value = "file"
                    },
                    modifier = Modifier.height(40.dp))
                {
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

                TextButton(
                    onClick = {
                        dialogMode.value = "add"
                        dialogType.value = "folder"
                    },
                    modifier = Modifier.height(40.dp))
                {
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
            LazyColumn {
                items(fileVM.getFileList(parentLevel.value)) {
                    Card(
                        modifier = Modifier
                            .clickable {
                                selectedItemIdx.value = fileVM.getFileIdx(it)
                                if (it.isFolder) {
                                    parentLevel.value = it.id
                                } else {
                                    dialogMode.value = "editContent"
                                }
                            }
                            .padding(vertical = 5.dp)
                            .border( width = 0.5.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(10.dp)
                            )
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
                                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                                    )
                                    Row() {
                                        Icon(
                                            imageVector = Icons.Filled.Edit, contentDescription = "Edit",

                                            modifier = Modifier.clickable {
                                                selectedItemIdx.value = fileVM.getFileIdx(it)
                                                dialogMode.value = "edit"
                                            },
                                            tint = Color.DarkGray
                                        )
                                        Icon(
                                            imageVector = Icons.Filled.Delete, contentDescription = "Delete",

                                            modifier = Modifier.clickable {
                                                fileVM.removeFileItem(it);
                                            },
                                            tint = Color.Red
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


