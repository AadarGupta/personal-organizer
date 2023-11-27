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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.util.debug.*
import java.util.*

fun checkColour(stack: ArrayDeque<String>): Long {
    return if (stack.isNotEmpty()) {
        0xFF67c2b3
    } else {
        0x80D3D3D3
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditPage(
    fileItemIdx: Int,
    fileVM: FileViewModel,
    dialogMode: MutableState<String>,
) {
    var fileItem = fileVM.getFileByIdx(fileItemIdx)
    var query = remember { mutableStateOf(fileItem.fileContent) }

    var lastState = remember { fileItem.fileContent }
    val undoStack = remember { ArrayDeque<String>() }
    val redoStack = remember { ArrayDeque<String>() }

    fun updateStacks(newText: String, oldText: String) {
        // Check if a word boundary (space or punctuation) was added
        if ((newText.length > oldText.length) && newText.last().isWhitespace() || newText.last() in listOf('.', ',', '?', '!') && !undoStack.contains(oldText)) {
            undoStack.addFirst(oldText)
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.addFirst(query.value)
            query.value = undoStack.removeFirst()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.addFirst(query.value)
            query.value = redoStack.removeFirst()
        }
    }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .onPreviewKeyEvent { keyEvent ->
                    if(keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.S && (keyEvent.isCtrlPressed || keyEvent.isMetaPressed)) {
                        fileVM.editFileContent(fileItem, query.value)
                        dialogMode.value = "preview"
                        true
                    } else if(keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Z && keyEvent.isShiftPressed  && (keyEvent.isCtrlPressed || keyEvent.isMetaPressed)) {
                        redo()
                        true
                    } else if(keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Z && (keyEvent.isCtrlPressed || keyEvent.isMetaPressed)) {
                        undo()
                        true
                    } else {
                        false
                    }
                },
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = fileItem.fileName,
                    fontSize = 40.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )

                Row {
                    Column(modifier = Modifier
                        .clickable { undo() }
                        .padding(horizontal = 20.dp)) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack, contentDescription = "Undo",
                            tint = Color(checkColour(undoStack)),
                            modifier = Modifier.height(40.dp)
                        )
                        Text(
                            text = "Undo",
                            fontSize = 10.sp,
                            color = Color(checkColour(undoStack)),
                        )
                    }

                    Column(modifier = Modifier
                        .clickable { redo() }
                        .padding(horizontal = 20.dp)) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward, contentDescription = "Redo",
                            tint = Color(checkColour(redoStack)),
                            modifier = Modifier.height(40.dp)
                        )
                        Text(
                            text = "Redo",
                            fontSize = 10.sp,
                            color = Color(checkColour(redoStack)),
                        )
                    }

                    Column(modifier = Modifier
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
                        Text(
                            text = "Preview",
                            fontSize = 10.sp,
                            color = Color(0xFF67c2b3),
                        )
                    }
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
                        onValueChange = {
                            val oldValue = query.value
                            query.value = it
                            updateStacks(it, oldValue) },
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 0.dp),
                    )
                }
            }


        }
    }