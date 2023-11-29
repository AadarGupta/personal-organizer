package com.example.models.db

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object FileDbObject: IntIdTable() {
    var owner = varchar("owner", 50)
    var isFolder = bool("isFolder")
    var parent = integer("parent")
    var fileName = varchar("fileName", 50)
    var fileContent = varchar("fileContent", 2000)
}


@Serializable
data class FileDbModel(
    val id: Int,
    var owner: String,
    var isFolder: Boolean,
    var parent: Int,
    var fileName: String,
    var fileContent: String
)