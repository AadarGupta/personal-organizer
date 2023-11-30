package sidebar.pomodoro;

import androidx.compose.runtime.mutableStateOf

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