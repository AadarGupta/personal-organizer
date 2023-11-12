package com.example.models.http

import kotlinx.serialization.Serializable

@Serializable
data class FileItem(
    val id: Int,
    var isFolder: Boolean,
    var parent: Int,
    var fileName: String,
    var fileContent: String
)

@Serializable
data class FileCreationRequest(
    var isFolder: Boolean,
    var parent: Int,
    var fileName: String,
    var fileContent: String
)

@Serializable
data class FileEditRequest(
    val id: Int,
    var isFolder: Boolean,
    var parent: Int,
    var fileName: String,
    var fileContent: String
)

@Serializable
data class FileListResponse(val items: MutableList<FileItem>)