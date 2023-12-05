package files

import kotlinx.serialization.Serializable

// File Model with an id, owner (name), isFolder (true or false), parent (level id), fileName (name), fileContent (content of the file => long string)
@Serializable
data class FileModel(
    val id: Int,
    val owner: String,
    val isFolder: Boolean,
    val parent: Int,
    val fileName: String,
    val fileContent: String
)