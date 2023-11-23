package files

import kotlinx.serialization.Serializable

@Serializable
data class FileModel(
    val id: Int,
    val owner: String,
    val isFolder: Boolean,
    val parent: Int,
    val fileName: String,
    val fileContent: String
)