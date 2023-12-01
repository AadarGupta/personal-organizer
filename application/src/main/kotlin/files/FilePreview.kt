package files

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun FilePreview(
    editState: MutableState<String>,
    fileItemIdx: Int,
    fileVM: FileViewModel
) {
    var fileItem = fileVM.getFileByIdx(fileItemIdx)
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