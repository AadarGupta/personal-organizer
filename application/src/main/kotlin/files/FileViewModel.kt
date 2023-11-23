package files

import MyHttp
import androidx.compose.runtime.mutableStateListOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive


@Serializable
data class FileList(val items: List<FileModel>)

class FileViewModel {
    private var fileList = mutableStateListOf<FileModel>()

    init {
        val http = MyHttp()
        val getAllFilesResponse : String = http.get("files")
        println(getAllFilesResponse)
        val obj = Json.decodeFromString<FileList>(getAllFilesResponse)

        for (file in obj.items) {
            fileList.add(file)
        }
    }

    fun getFileList(parentLevel: Int): List<FileModel> {
        val filteredList = fileList.filter { it.parent == parentLevel }
        return filteredList
    }

    fun isEmpty(): Boolean {
        return fileList.isEmpty()
    }

    fun addFile(
        folder: Boolean,
        parentId: Int,
        name: String
    ): Int {

        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "isFolder" to JsonPrimitive(folder),
                "fileName" to JsonPrimitive(name),
                "parent" to JsonPrimitive(parentId),
                "fileContent" to JsonPrimitive("")
            )
        )

        val createFileResponse = http.post("file", body)
        val newItem = Json.decodeFromString<FileModel>(createFileResponse.body())
        fileList.add(newItem)

        return fileList.size - 1
    }

    fun editFileName(
        targetItem: FileModel,
        newName: String
    ) {
        val idx = fileList.indexOf(targetItem)
        fileList[idx] =
            FileModel(
                fileList[idx].id,
                fileList[idx].isFolder,
                fileList[idx].parent,
                newName,
                fileList[idx].fileContent
            )

        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "id" to JsonPrimitive(targetItem.id),
                "isFolder" to JsonPrimitive(targetItem.isFolder),
                "parent" to JsonPrimitive(targetItem.parent),
                "fileName" to JsonPrimitive(newName),
                "fileContent" to JsonPrimitive(targetItem.fileContent)
            )
        )
        http.put("file", body)
    }

    fun editFileContent(
        targetItem: FileModel,
        newContent: String
    ) {
        val idx = fileList.indexOf(targetItem)
        fileList[idx] =
            FileModel(
                fileList[idx].id,
                fileList[idx].isFolder,
                fileList[idx].parent,
                fileList[idx].fileName,
                newContent
            )

        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "id" to JsonPrimitive(targetItem.id),
                "isFolder" to JsonPrimitive(targetItem.isFolder),
                "parent" to JsonPrimitive(targetItem.parent),
                "fileName" to JsonPrimitive(targetItem.fileName),
                "fileContent" to JsonPrimitive(newContent)
            )
        )
        http.put("file", body)
    }

    fun removeFileItem(targetItem: FileModel) {
        val http = MyHttp()
        http.delete("file", mapOf("id" to targetItem.id.toString()))
        fileList.remove(targetItem)
    }

    fun moveFile(
        targetItem: FileModel,
        newParent: Int
    ) {
        val idx = fileList.indexOf(targetItem)
        fileList[idx] =
            FileModel(
                fileList[idx].id,
                fileList[idx].isFolder,
                newParent,
                fileList[idx].fileName,
                fileList[idx].fileContent
            )

        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "id" to JsonPrimitive(targetItem.id),
                "isFolder" to JsonPrimitive(targetItem.isFolder),
                "parent" to JsonPrimitive(newParent),
                "fileName" to JsonPrimitive(targetItem.fileName),
                "fileContent" to JsonPrimitive(targetItem.fileContent)
            )
        )
        http.put("file", body)
    }

    fun getFileByIdx(idx: Int): FileModel {
        return fileList[idx]
    }

    fun getFileById(id: Int): FileModel {
        return fileList.find { it.id == id }!!
    }

    fun getFileIdx(fileItem: FileModel): Int {
        return fileList.indexOf(fileItem)
    }
}