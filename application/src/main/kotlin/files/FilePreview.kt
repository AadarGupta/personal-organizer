package files

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window

// File Preview (when user clicks on the file card)
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun FilePreview(
    editState: MutableState<String>,
    fileItemIdx: Int,
    fileVM: FileViewModel
) {
    // By default, opens in preview mode
    val dialogMode = remember { mutableStateOf("preview") }

    // Close window, when the "x" is pressed
    Window(onCloseRequest = { editState.value = "closed" })
    {
        // If mode is preview, open the preview page
        if(dialogMode.value == "preview") {
            PreviewPage(fileItemIdx, fileVM, dialogMode)
        }
        // If the mode is edit, open the edit page
        else if (dialogMode.value == "edit") {
            EditPage(fileItemIdx, fileVM, dialogMode)
        }
    }
}