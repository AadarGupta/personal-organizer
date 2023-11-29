package sidebar.pomodoro

import kotlinx.serialization.Serializable

@Serializable
data class PomodoroModel(val id: Int, val breaktime: Int, val worktime: Int, val isChecked: Boolean) {}