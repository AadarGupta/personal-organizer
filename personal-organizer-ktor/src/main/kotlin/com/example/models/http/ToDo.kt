package com.example.models.http

import kotlinx.serialization.Serializable

@Serializable
data class ToDoItem(val id: Int, val itemName: String, val isChecked: Boolean)

@Serializable
data class ToDoCreationRequest(val name: String, val isChecked: Boolean)

@Serializable
data class ToDoEditNameRequest(val id: Int, val name: String)

@Serializable
data class ToDoEditCheckedRequest(val id: Int, val isChecked: Boolean)

@Serializable
data class ToDoListResponse(val items: MutableList<ToDoItem>)
