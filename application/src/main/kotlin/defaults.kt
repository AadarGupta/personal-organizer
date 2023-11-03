import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction


fun resetDefaultToDos() {
    transaction {
        ToDoDataObject.insert {
            it[itemName] = "Get groceries"
            it[isChecked] = false
        }
        ToDoDataObject.insert {
            it[itemName] = "Plan a heist"
            it[isChecked] = false
        }
        ToDoDataObject.insert {
            it[itemName] = "Learn how to walk"
            it[isChecked] = true
        }
        ToDoDataObject.insert {
            it[itemName] = "Hack NASA"
            it[isChecked] = true
        }
    }
}

fun resetDefaultReminders() {
    transaction {

        ReminderDataObject.insert {
            it[itemName] = "Pay hydro"
            it[year] = "2023"
            it[month] = "12"
            it[day] = "12"
            it[time] = "12:12:12"
            it[isChecked] = true
        }
        ReminderDataObject.insert {
            it[itemName] = "Pay rent"
            it[year] = "2023"
            it[month] = "12"
            it[day] = "12"
            it[time] = "12:12:12"
            it[isChecked] = true
        }
        ReminderDataObject.insert {
            it[itemName] = "Go to bed earlier"
            it[year] = "2023"
            it[month] = "12"
            it[day] = "12"
            it[time] = "12:12:12"
            it[isChecked] = true
        }
    }
}

fun resetDefaultFiles() {
    transaction {

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "root"
            it[fileName] = "File 1"
            it[fileContent] = "File Content 1"
        }

        FileDataObject.insert {
            it[isFolder] = true
            it[parent] = "root"
            it[fileName] = "Folder 1"
            it[fileContent] = ""
        }

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "Folder 1"
            it[fileName] = "File 1.1"
            it[fileContent] = "File Content 1.1"
        }

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "Folder 1"
            it[fileName] = "File 1.2"
            it[fileContent] = "File Content 1.2"
        }

        FileDataObject.insert {
            it[isFolder] = true
            it[parent] = "Folder 1"
            it[fileName] = "Folder 1.1"
            it[fileContent] = ""
        }

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "Folder 1.1"
            it[fileName] = "File 1.1.1"
            it[fileContent] = "File Content 1.1.1"
        }

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "root"
            it[fileName] = "File 2"
            it[fileContent] = "File Content 2"
        }

        FileDataObject.insert {
            it[isFolder] = false
            it[parent] = "root"
            it[fileName] = "File 3"
            it[fileContent] = "File Content 3"
        }
    }
}

