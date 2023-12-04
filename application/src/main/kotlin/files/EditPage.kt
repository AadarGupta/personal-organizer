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
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditPage(
    fileItemIdx: Int,
    fileVM: FileViewModel,
    dialogMode: MutableState<String>,
) {
    var fileItem = fileVM.getFileByIdx(fileItemIdx)
    val query = remember { mutableStateOf(TextFieldValue(fileItem.fileContent)) }

    val undoStack = remember { ArrayDeque<TimedTextField>() }
    val redoStack = remember { ArrayDeque<TimedTextField>() }
    val focusRequester = remember { FocusRequester() }

    val openHelp = remember { mutableStateOf("closed") }

    if (openHelp.value == "open") {
        HelpMenu(openHelp)
    }

    fun checkColour(stack: ArrayDeque<TimedTextField>): Long {
        return if (stack.isNotEmpty()) {
            0xFF67c2b3
        } else {
            0x80D3D3D3
        }
    }

    fun updateStacks(newText: String, oldText: TextFieldValue) {
        val currentTime = System.currentTimeMillis()
        val newTimedText = TimedTextField(oldText, currentTime)
        // Check if the text has been changed
        if (newText != oldText.text) {
            val lastChangeTime = if (undoStack.isNotEmpty()) undoStack.first().timestamp else 0
            // Group changes based on time threshold (e.g., 2000 milliseconds = 2 seconds)
            if (currentTime - lastChangeTime > 2000) {
                // Add the old text to the undo stack if significant time has passed
                undoStack.addFirst(newTimedText)
            }
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            // Take the current value to redo stack
            val currentTimedText = TimedTextField(query.value, System.currentTimeMillis())
            redoStack.addFirst(currentTimedText)

            // Get the previous value from undo stack and apply it
            val previousTimedText = undoStack.removeFirst()
            query.value = previousTimedText.textFieldValue
        }
    }


    fun redo() {
        if (redoStack.isNotEmpty()) {
            // Take the current value to undo stack
            val currentTimedText = TimedTextField(query.value, System.currentTimeMillis())
            undoStack.addFirst(currentTimedText)

            // Get the next value from redo stack and apply it
            val nextTimedText = redoStack.removeFirst()
            query.value = nextTimedText.textFieldValue
        }
    }


    Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .onPreviewKeyEvent { keyEvent ->
                    if(keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.S && (keyEvent.isCtrlPressed || keyEvent.isMetaPressed)) {
                        fileVM.editFileContent(fileItem, query.value.text)
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
                        .clickable {
                            openHelp.value = "open"
                        }
                        .padding(horizontal = 20.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Info, contentDescription = "Markdown Commands",
                            tint = Color(0xFF67c2b3),
                            modifier = Modifier.height(40.dp)
                        )
                        Text(
                            text = "Help",
                            fontSize = 10.sp,
                            color = Color(0xFF67c2b3),
                        )
                    }

                    Column(modifier = Modifier
                        .clickable(enabled = undoStack.isNotEmpty()) {
                            undo()
                        }
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
                        .clickable(enabled = redoStack.isNotEmpty()) {
                            redo()
                        }
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
                            fileVM.editFileContent(fileItem, query.value.text)
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
                            query.value = it.copy(text = it.text)
                            updateStacks(it.text, oldValue)
                        },
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 0.dp)
                            .focusRequester(focusRequester),
                    )
                }
            }


        }
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                focusRequester.requestFocus()
                query.value = query.value.copy(
                    selection = TextRange(query.value.text.length)
                )
            }
        }
    }