package sidebar.todos

import org.jetbrains.exposed.dao.id.EntityID

class ToDoModel(id: EntityID<Int>, itemName: String, isChecked: Boolean) {
    var id = id
    var itemName = itemName
    var isChecked = isChecked
}