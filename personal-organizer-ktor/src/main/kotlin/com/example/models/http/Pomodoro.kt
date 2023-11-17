package com.example.models.http

import kotlinx.serialization.Serializable

@Serializable
data class PomodoroItem(val id: Int, val breaktime: Int, val worktime: Int, val isChecked: Boolean)

@Serializable
data class PomodoroCreationRequest(val breaktime: Int, val worktime: Int, val isChecked: Boolean)

@Serializable
data class PomodoroEditCheckedRequest(val id: Int, val isChecked: Boolean)

@Serializable
data class PomodoroListResponse(val items: MutableList<PomodoroItem>)
