package sidebar.todos

import kotlinx.serialization.Serializable

// To do data model.
@Serializable
data class ToDoModel(
    val id: Int,
    val owner: String,
    val itemName: String,
    val isChecked: Boolean
) {}