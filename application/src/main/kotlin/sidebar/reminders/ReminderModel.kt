package sidebar.reminders

import kotlinx.serialization.Serializable

@Serializable
data class ReminderModel(
    val id: Int,
    val itemName: String,
    val year: String,
    val month: String,
    val day: String,
    val time: String
)
