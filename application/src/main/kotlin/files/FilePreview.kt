package files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilePreview(
    editState: MutableState<String>,
    fileItemIdx: Int,
    fileVM: FileViewModel
) {
    var fileItem = fileVM.getFileByIdx(fileItemIdx)
    var query = remember { mutableStateOf(fileItem.fileContent) }
    val dialogMode = remember { mutableStateOf("preview") }

    Window(onCloseRequest = { editState.value = "closed" })
    {
        if(dialogMode.value == "preview") {
            PreviewPage(fileItemIdx, fileVM, dialogMode)
        } else if (dialogMode.value == "edit") {
            EditPage(fileItemIdx, fileVM, dialogMode)
        }
    }
}