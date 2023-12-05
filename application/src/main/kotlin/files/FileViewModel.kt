package files

import MyHttp
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import io.ktor.client.call.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive


// Creates a file list class using File Model
@Serializable
data class FileList(val items: List<FileModel>)

// Creates a File View Model for a user
class FileViewModel(currUser: MutableState<String>) {
    // Sets the current user and a file list for that user
    var currUser = currUser
    private var fileList = mutableStateListOf<FileModel>()

    // Initializes a FileViewModel
    init {
        // Start a blocking operation on the main thread
        runBlocking {
            // Launch a coroutine for performing tasks asynchronously
            launch {
                // Create an instance of a custom HTTP client and make a get request for the current user
                val http = MyHttp()
                val getAllFilesResponse = http.get("files?user=${currUser.value}")
                // Parse the json as a FileList object
                val obj = Json.decodeFromString<FileList>(getAllFilesResponse.body())

                // Loop through the obtained data and add to fileList
                for (file in obj.items) {
                    fileList.add(file)
                }
            }
        }
    }

    // Return the list of files for a particular parent level
    fun getFileList(parentLevel: Int): List<FileModel> {
        val filteredList = fileList.filter { it.parent == parentLevel }
        return filteredList
    }

    // Check if the list of files is empty
    fun isEmpty(): Boolean {
        return fileList.isEmpty()
    }

    // Add a file
    fun addFile(
        folder: Boolean,
        parentId: Int,
        name: String
    ): Int {

        // Start a blocking operation on the main thread
        runBlocking {
            // Launch a coroutine for performing tasks asynchronously
            launch {

                // Create an instance of a custom HTTP client and create a JSON object to replicate a file
                val http = MyHttp()
                val body = JsonObject(
                    mapOf(
                        "isFolder" to JsonPrimitive(folder),
                        "fileName" to JsonPrimitive(name),
                        "parent" to JsonPrimitive(parentId),
                        "fileContent" to JsonPrimitive("")
                    )
                )

                // Make a post request to send the data for the current user and get the new file from JSON
                val createFileResponse = http.post("file?user=${currUser.value}", body)
                val newItem = Json.decodeFromString<FileModel>(createFileResponse.body())
                // Add the new file to file list
                fileList.add(newItem)
            }
        }

        // Return the index of the file
        return fileList.size - 1
    }

    // Edit the name of a file
    fun editFileName(
        targetItem: FileModel,
        newName: String
    ) {
        // Get the index of the file in the list and change its name to new name
        val idx = fileList.indexOf(targetItem)
        fileList[idx] =
            FileModel(
                fileList[idx].id,
                currUser.value,
                fileList[idx].isFolder,
                fileList[idx].parent,
                newName,
                fileList[idx].fileContent
            )

        // Start a blocking operation on the main thread
        runBlocking {
            // Launch a coroutine for performing tasks asynchronously
            launch {
                // Create an instance of a custom HTTP client and create a JSON object to replicate a file
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
                // Replaces the data for the name
                http.put("file?user=${currUser.value}", body)
            }
        }
    }

    // Edit the contents of the file
    fun editFileContent(
        targetItem: FileModel,
        newContent: String
    ) {

        // Get the index of the file in the list and change its content to new content
        val idx = fileList.indexOf(targetItem)
        fileList[idx] =
            FileModel(
                fileList[idx].id,
                currUser.value,
                fileList[idx].isFolder,
                fileList[idx].parent,
                fileList[idx].fileName,
                newContent
            )

        // Start a blocking operation on the main thread
        runBlocking {
            // Launch a coroutine for performing tasks asynchronously
            launch {
                // Create an instance of a custom HTTP client and create a JSON object to replicate a file
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
                // Replaces the data for the content
                http.put("file?user=${currUser.value}", body)
            }
        }
    }

    // Removes a file
    fun removeFileItem(targetItem: FileModel) {
        // Start a blocking operation on the main thread
        runBlocking {
            // Launch a coroutine for performing tasks asynchronously
            launch {
                // Creates an instance of a custom HTTP client and deletes the file from database
                val http = MyHttp()
                http.delete(
                    "file",
                    mapOf(
                        "id" to targetItem.id.toString(),
                        "user" to currUser.value
                    )
                )
                // Removed the item from file list
                fileList.remove(targetItem)
            }
        }
    }

    // Moves the file from current parent to targeted parent
    fun moveFile(
        targetItem: FileModel,
        newParent: Int
    ) {
        // Get the index of the file in the list and change its parent to new parent
        val idx = fileList.indexOf(targetItem)
        fileList[idx] =
            FileModel(
                fileList[idx].id,
                currUser.value,
                fileList[idx].isFolder,
                newParent,
                fileList[idx].fileName,
                fileList[idx].fileContent
            )

        // Start a blocking operation on the main thread
        runBlocking {
            // Launch a coroutine for performing tasks asynchronously
            launch {
                // Create an instance of a custom HTTP client and create a JSON object to replicate a file
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
                // Replaces the data for the parent
                http.put("file?user=${currUser.value}", body)
            }
        }
    }

    // Returns the file by its index
    fun getFileByIdx(idx: Int): FileModel {
        return fileList[idx]
    }

    // Returns the file based on the id
    fun getFileById(id: Int): FileModel {
        return fileList.find { it.id == id }!!
    }

    // Returns the index of the file
    fun getFileIdx(fileItem: FileModel): Int {
        return fileList.indexOf(fileItem)
    }
}