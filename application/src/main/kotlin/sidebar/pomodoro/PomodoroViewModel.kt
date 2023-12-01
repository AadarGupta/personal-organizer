package sidebar.pomodoro;

import androidx.compose.runtime.mutableStateOf

class PomodoroViewModel() {
    private var curPomodoro = mutableStateOf<PomodoroModel>(PomodoroModel(5, 25))
    init {
        curPomodoro.value.worktime = 25;
        curPomodoro.value.breaktime = 5;

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