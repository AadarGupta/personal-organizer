import org.jetbrains.exposed.dao.id.IntIdTable


object FileDataObject: IntIdTable() {
    var fileName = varchar("fileName", 50)
    var fileContent = varchar("fileContent", 2000)
}

object ReminderDataObject: IntIdTable() {
    var itemName = varchar("itemName", 50)
}

object ToDoDataObject: IntIdTable() {
    var itemName = varchar("itemName", 50)
}

