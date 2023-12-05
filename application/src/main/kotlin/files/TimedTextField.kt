package files

import androidx.compose.ui.text.input.TextFieldValue

// Class for a TextFieldValue with a timestamp (for undo/redo purposes)
data class TimedTextField(val textFieldValue: TextFieldValue, val timestamp: Long)
