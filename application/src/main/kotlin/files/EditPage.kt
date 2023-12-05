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

// Editable Page (when user opens page and clicks edit)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditPage(
    fileItemIdx: Int,
    fileVM: FileViewModel,
    dialogMode: MutableState<String>,
) {
    // Gets the file based on the passed in index and loads its contents
    var fileItem = fileVM.getFileByIdx(fileItemIdx)
    val query = remember { mutableStateOf(TextFieldValue(fileItem.fileContent)) }

    // Generates an undo and redo stack
    val undoStack = remember { ArrayDeque<TimedTextField>() }
    val redoStack = remember { ArrayDeque<TimedTextField>() }
    // Requests focus to autofocus the TextField
    val focusRequester = remember { FocusRequester() }
    // Sets the help menu to a default of closed
    val openHelp = remember { mutableStateOf("closed") }

    // Opens help menu if openHelp is open
    if (openHelp.value == "open") {
        HelpMenu(openHelp)
    }

    // Checks if the colour should be enabled
    fun checkColour(stack: ArrayDeque<TimedTextField>): Long {
        // If the stack is not empty, make the button green
        return if (stack.isNotEmpty()) {
            0xFF67c2b3
        } else {
            // Greyed out if the stack is empty
            0x80D3D3D3
        }
    }

    // Updated the undo stack based on the newText and the oldText
    fun updateStacks(newText: String, oldText: TextFieldValue) {
        // Calculates current time in milliseconds
        val currentTime = System.currentTimeMillis()
        // Creates a timed text field value based on the value with the current time
        val newTimedText = TimedTextField(oldText, currentTime)
        // Checks if the text has been changed
        if (newText != oldText.text) {
            // If the undoStack is not empty, get the timestamp of the last change
            val lastChangeTime = if (undoStack.isNotEmpty()) undoStack.first().timestamp else 0
            // Group changes based on time threshold (2 seconds)
            if (currentTime - lastChangeTime > 2000) {
                // Add the old text to the undo stack if significant time has passed (2000 milliseconds)
                undoStack.addFirst(newTimedText)
            }
        }
    }

    // Undo function when button is pressed
    fun undo() {
        // Check if undo is possible
        if (undoStack.isNotEmpty()) {
            // Take the current value and add to redo stack
            val currentTimedText = TimedTextField(query.value, System.currentTimeMillis())
            redoStack.addFirst(currentTimedText)

            // Get the previous value and apply it
            val previousTimedText = undoStack.removeFirst()
            query.value = previousTimedText.textFieldValue
        }
    }

    // Redo function when button is pressed
    fun redo() {
        // Check if redo is possible
        if (redoStack.isNotEmpty()) {
            // Take the current value and add to undo stack
            val currentTimedText = TimedTextField(query.value, System.currentTimeMillis())
            undoStack.addFirst(currentTimedText)

            // Get the previous value and apply it
            val nextTimedText = redoStack.removeFirst()
            query.value = nextTimedText.textFieldValue
        }
    }


    // Generates UI for the edit page
    Column(
            // Column of a max size, with vertical scrolling and spaced by 15 dp
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .onPreviewKeyEvent { keyEvent ->
                    // Handles the hotkey commands (save, undo, redo)
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
            // Heading row with title and commands
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

                    // Button for modified markdown commands
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

                    // Button for undo command
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

                    // Button for redo command
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

                    // Button to preview rendered markdown
                    Column(modifier = Modifier
                        .clickable {
                            // Edits content and saves it + opens preview
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



            // Divider row (separation between header and editable area)
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 100.dp)
            ) {
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color.Black)
            }

            // Editable row with textfield
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // TextField where user enters markdown
                    BasicTextField(
                        // Value changes and updates the undo stack
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

        // Performs this on load
        LaunchedEffect(Unit) {
            // Runs this in the background thread
            withContext(Dispatchers.IO) {
                // Immediately focus on TextField
                focusRequester.requestFocus()
                // Set the cursor to the end of the text
                query.value = query.value.copy(
                    selection = TextRange(query.value.text.length)
                )
            }
        }
    }