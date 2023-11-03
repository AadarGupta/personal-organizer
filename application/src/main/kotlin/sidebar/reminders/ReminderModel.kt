package sidebar.reminders

import org.jetbrains.exposed.dao.id.EntityID

class ReminderModel(id: EntityID<Int>, itemName: String, year: String, month: String, day: String, time: String, isChecked: Boolean) {
    var id = id
    var itemName = itemName
    var year = year
    var month = month
    var day = day
    var time = time
    var isChecked = isChecked
}