package sidebar.pomodoro

import kotlinx.serialization.Serializable

@Serializable
data class PomodoroModel(var breaktime: Int, var worktime: Int) {}