package sidebar.todos

import kotlinx.serialization.Serializable

@Serializable
data class ToDoModel(
    val id: Int,
    val owner: String,
    val itemName: String,
    val isChecked: Boolean
) {}