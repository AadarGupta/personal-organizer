package files

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MoveDialog(
    mode: MutableState<String>,
    parentLevel: MutableState<Int>,
    fileItemIdx: Int,
    fileVM: FileViewModel
) {
    var fileItem = fileVM.getFileByIdx(fileItemIdx)

    Dialog(onCloseRequest = { mode.value = "closed" }, title = "Move ${fileItem.fileName}")
    {
        TextButton(
            onClick = {
                fileVM.moveFile(fileItem, -1)
                mode.value = "closed"
            },
            modifier = Modifier.height(40.dp))
        {
            Image(
                painter = painterResource("folderIcon.png"),
                contentDescription = "Folder Icon"
            )
            Text(
                text = "..",
                fontSize = 15.sp,
                color = Color.Black,
            )
        }
        LazyColumn(modifier = Modifier.padding(vertical = 40.dp)) {
            items(fileVM.getFileList(parentLevel.value)) {
                if(it.isFolder && it != fileItem) {
                    TextButton(
                        onClick = {
                            fileVM.moveFile(fileItem, it.id)
                            mode.value = "closed"
                        },
                        modifier = Modifier.height(40.dp))
                    {
                        Image(
                            painter = painterResource("folderIcon.png"),
                            contentDescription = "Folder Icon"
                        )
                        Text(
                            text = it.fileName,
                            fontSize = 15.sp,
                            color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}