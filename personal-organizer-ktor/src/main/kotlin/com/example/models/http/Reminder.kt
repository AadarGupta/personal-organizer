package com.example.models.http

import kotlinx.serialization.Serializable

@Serializable
data class ReminderItem(
    val id: Int,
    var owner: String,
    val itemName: String,
    val year: String,
    val month: String,
    val day: String,
    val time: String,
    val isChecked: Boolean
)

@Serializable
data class ReminderCreationRequest(
    val name: String,
    val year: String,
    val month: String,
    val day: String,
    val time: String,
    val isChecked: Boolean
)

@Serializable
data class ReminderEditRequest(
    val id: Int,
    val name: String,
    val year: String,
    val month: String,
    val day: String,
    val time: String
)

@Serializable
data class ReminderEditCheckedRequest(val id: Int, val isChecked: Boolean)

@Serializable
data class ReminderListResponse(val items: MutableList<ReminderItem>)
