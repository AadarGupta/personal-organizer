import org.jetbrains.exposed.dao.id.IntIdTable


object FileDataObject: IntIdTable() {
    var isFolder = bool("isFolder")
    var parent = varchar("parent", 50)
    var fileName = varchar("fileName", 50)
    var fileContent = varchar("fileContent", 2000)
}

object ReminderDataObject: IntIdTable() {
    var itemName = varchar("itemName", 50)
    var year = varchar("year", 50)
    var month = varchar("month", 50)
    var day = varchar("day", 50)
    var time = varchar("time", 50)
    var isChecked = bool("isChecked")
}

object ToDoDataObject: IntIdTable() {
    var itemName = varchar("itemName", 50)
    var isChecked = bool("isChecked")
}

