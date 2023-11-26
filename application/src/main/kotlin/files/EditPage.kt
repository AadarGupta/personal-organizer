package files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditPage(
    fileItemIdx: Int,
    fileVM: FileViewModel,
    dialogMode: MutableState<String>,
) {
    var fileItem = fileVM.getFileByIdx(fileItemIdx)
    var query = remember { mutableStateOf(fileItem.fileContent) }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .onPreviewKeyEvent { keyEvent ->
                    if(keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.S && (keyEvent.isCtrlPressed || keyEvent.isMetaPressed)) {
                        fileVM.editFileContent(fileItem, query.value)
                        dialogMode.value = "preview"
                        true
                    } else {
                        false
                    }
                },
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                Text(
                    text = fileItem.fileName,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(horizontal = 15.dp).padding(top = 10.dp, bottom = 0.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )

                Column( modifier = Modifier
                    .clickable {
                        fileVM.editFileContent(fileItem, query.value)
                        dialogMode.value = "preview"
                    }
                    .padding(horizontal = 20.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Done, contentDescription = "Preview",
                        tint = Color(0xFF67c2b3),
                        modifier = Modifier.height(40.dp)
                    )
                    Text (
                        text = "Preview",
                        fontSize = 10.sp,
                        color = Color(0xFF67c2b3),
                    )
                }


            }

            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 100.dp)
            ) {
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color.Black)
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