package files

import FileDataObject
import androidx.compose.runtime.mutableStateListOf
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.reflect.jvm.internal.impl.load.kotlin.KotlinClassFinder.Result.ClassFileContent


class FileViewModel {
    private var fileList = mutableStateListOf<FileModel>()

    init {
        transaction {
            for (file in FileDataObject.selectAll()) {
                val fileData =
                    FileModel(file[FileDataObject.id], file[FileDataObject.isFolder], file[FileDataObject.parent], file[FileDataObject.fileName], file[FileDataObject.fileContent])
                fileList.add(fileData)
            }
        }
    }

    fun getFileList(): List<FileModel> {
        return fileList
    }

    fun isFileEmpty(): Boolean {
        return fileList.isEmpty()
    }

    fun addFileList(folder: Boolean, parentFolder: String, name: String, content: String): Int {
        transaction {
            val newFile = FileDataObject.insert {
                it[fileName] = name
                it[isFolder] = folder
                it[parent] = parentFolder
                it[fileContent] = content
            } get FileDataObject.id

            fileList.add(FileModel(newFile, folder,parentFolder, name,content))
        }
        return fileList.size - 1
    }

    fun editFileList(fileItem: FileModel, value: String) {
        val idx = fileList.indexOf(fileItem)
        fileList[idx] = FileModel(fileList[idx].id, fileList[idx].isFolder, fileList[idx].parent, fileList[idx].fileName, fileList[idx].fileContent )
        transaction {
            FileDataObject.update({ FileDataObject.id eq fileItem.id }) {
                it[fileContent] = value
            }
        }
    }

    fun removeFileItem(fileItem: FileModel) {

        fileList.remove(fileItem)
        transaction {
            FileDataObject.deleteWhere { FileDataObject.id eq fileItem.id }
        }
    }

    fun getFileByIdx(idx: Int): FileModel {
        return fileList[idx]
    }

    fun getFileIdx(fileItem: FileModel): Int {
        return fileList.indexOf(fileItem)
    }
}