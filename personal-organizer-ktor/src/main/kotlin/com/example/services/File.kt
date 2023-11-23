package com.example.services

import com.example.models.db.FileDbModel
import com.example.models.db.FileDbObject
import com.example.models.http.FileItem
import com.example.models.http.FileListResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun createFile(
    isFolderStatus: Boolean,
    itemOwner: String,
    newParent: Int,
    newFileName: String,
    newFileContent: String
) : FileDbModel {
    var targetFileItem = FileDbModel(-1, itemOwner,false, -1, "", "")

    transaction {
        val newFile =
            FileDbObject.insert {
                it[owner] = itemOwner
                it[isFolder] = isFolderStatus
                it[parent] = newParent
                it[fileName] = newFileName
                it[fileContent] = newFileContent
            } get FileDbObject.id

        targetFileItem =
            FileDbModel(
                newFile.value,
                itemOwner,
                isFolderStatus,
                newParent,
                newFileName,
                newFileContent
            )
    }
    return targetFileItem
}

fun editFile(
    targetId: Int,
    itemOwner: String,
    isFolderStatus: Boolean,
    newParent: Int,
    newFileName: String,
    newFileContent: String
) {
    transaction {
        FileDbObject.update({ FileDbObject.id.eq(targetId) and FileDbObject.owner.eq(itemOwner)}) {
            it[isFolder] = isFolderStatus
            it[parent] = newParent
            it[fileName] = newFileName
            it[fileContent] = newFileContent
        }
    }
}


fun deleteFile(itemOwner: String, targetId: Int) {
    transaction {
        FileDbObject.deleteWhere { FileDbObject.id.eq(targetId) and FileDbObject.owner.eq(itemOwner) }
        // delete all that have this id as parent
        FileDbObject.deleteWhere { FileDbObject.parent.eq(targetId) and FileDbObject.owner.eq(itemOwner) }
    }
}


fun getFiles(itemOwner: String) : FileListResponse {
    val fileList = mutableListOf<FileItem>()
    transaction {
        for (file in FileDbObject.selectAll()) {
            if (file[FileDbObject.owner] == itemOwner) {
                val fileData =
                    FileItem(
                        file[FileDbObject.id].value,
                        file[FileDbObject.owner],
                        file[FileDbObject.isFolder],
                        file[FileDbObject.parent],
                        file[FileDbObject.fileName],
                        file[FileDbObject.fileContent]
                    )
                fileList.add(fileData)
            }
        }
    }
    val fileListResponse = FileListResponse(fileList)
    return fileListResponse
}