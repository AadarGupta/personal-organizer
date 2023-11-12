package com.example.services

import com.example.models.db.FileDbModel
import com.example.models.db.FileDbObject
import com.example.models.http.FileItem
import com.example.models.http.FileListResponse
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun createFile(
    isFolderStatus: Boolean,
    newParent: Int,
    newFileName: String,
    newFileContent: String
) : FileDbModel {
    var targetFileItem = FileDbModel(-1, false, -1, "", "")

    transaction {
        val newFile =
            FileDbObject.insert {
                it[isFolder] = isFolderStatus
                it[parent] = newParent
                it[fileName] = newFileName
                it[fileContent] = newFileContent
            } get FileDbObject.id

        targetFileItem =
            FileDbModel(
                newFile.value,
                isFolderStatus,
                newParent,
                newFileName,
                newFileContent
            )
    }
    return targetFileItem
}

fun editFile(
    id: Int,
    isFolderStatus: Boolean,
    newParent: Int,
    newFileName: String,
    newFileContent: String
) {
    transaction {
        FileDbObject.update({ FileDbObject.id eq id}) {
            it[isFolder] = isFolderStatus
            it[parent] = newParent
            it[fileName] = newFileName
            it[fileContent] = newFileContent
        }
    }
}


fun deleteFile(id: Int) {
    transaction {
        FileDbObject.deleteWhere { FileDbObject.id eq id }
        // delete all that have this id as parent
        FileDbObject.deleteWhere { FileDbObject.parent eq id }
    }
}


fun getFiles() : FileListResponse {
    val fileList = mutableListOf<FileItem>()
    transaction {
        for (file in FileDbObject.selectAll()) {
            val fileData =
                FileItem(
                    file[FileDbObject.id].value,
                    file[FileDbObject.isFolder],
                    file[FileDbObject.parent],
                    file[FileDbObject.fileName],
                    file[FileDbObject.fileContent]
                )
            fileList.add(fileData)
        }
    }
    val fileListResponse = FileListResponse(fileList)
    return fileListResponse
}