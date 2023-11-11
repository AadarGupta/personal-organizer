import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction


fun resetDefaultToDos() {
}

fun resetDefaultReminders() {
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

