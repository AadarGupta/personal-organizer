package files

import org.jetbrains.exposed.dao.id.EntityID

class FileModel(id: EntityID<Int>, isFolder: Boolean, parent: String, fileName: String, fileContent: String) {
    var id = id
    var isFolder = isFolder
    var parent = parent
    var fileName = fileName
    var fileContent = fileContent
}