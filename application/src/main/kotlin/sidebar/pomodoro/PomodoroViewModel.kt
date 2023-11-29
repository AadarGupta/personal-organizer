package sidebar.pomodoro;

import MyHttp
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import sidebar.reminders.ReminderList

class PomodoroViewModel() {
    private var curPomodoro = mutableStateOf<PomodoroModel>(PomodoroModel(5*60, 25*60))
    init {
        curPomodoro.value.worktime = 25*60;
        curPomodoro.value.breaktime = 5*60;

    }
    fun getPomodoro(): PomodoroModel {
        return curPomodoro.value;
    }

    fun editWorkTime(workT: Int) {
        curPomodoro.value.worktime = workT;
    }
    fun editBreakTime(breakT: Int) {
        curPomodoro.value.breaktime = breakT;
    }

}