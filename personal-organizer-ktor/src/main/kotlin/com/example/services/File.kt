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

        // create file item
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
        // update file
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
        // delete file
        FileDbObject.deleteWhere { FileDbObject.id.eq(targetId) and FileDbObject.owner.eq(itemOwner) }
        // delete all that have this id as parent
        FileDbObject.deleteWhere { FileDbObject.parent.eq(targetId) and FileDbObject.owner.eq(itemOwner) }
    }
}


fun getFiles(itemOwner: String) : FileListResponse {
    val fileList = mutableListOf<FileItem>()
    transaction {
        // get all files
        for (file in FileDbObject.selectAll()) {
            // check if file belongs to user
            if (file[FileDbObject.owner] == itemOwner) {
                // create file item
                val fileData =
                    FileItem(
                        file[FileDbObject.id].value,
                        file[FileDbObject.owner],
                        file[FileDbObject.isFolder],
                        file[FileDbObject.parent],
                        file[FileDbObject.fileName],
                        file[FileDbObject.fileContent]
                    )
                // add file item to list
                fileList.add(fileData)
            }
        }
    }

    // create file list response
    val fileListResponse = FileListResponse(fileList)
    return fileListResponse
}