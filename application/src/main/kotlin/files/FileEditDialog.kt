package files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FileEditDialog(
    editState: MutableState<String>,
    fileItemIdx: Int,
    fileVM: FileViewModel
) {
    var fileItem = fileVM.getFileByIdx(fileItemIdx)
    var query = remember { mutableStateOf(fileItem.fileContent) }

    Dialog(onCloseRequest = { editState.value = "closed" })
    {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                Text(
                    text = fileItem.fileName,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                    color = Color.Black,
                )

                Icon(
                    imageVector = Icons.Filled.Done, contentDescription = "Save",
                    modifier = Modifier
                        .clickable {
                            fileVM.editFileContent(fileItem, query.value)
                            editState.value = "closed"
                        }
                        .height(40.dp)
                        .padding(horizontal = 30.dp),
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