package sidebar.pomodoro;

import androidx.compose.runtime.mutableStateOf

// Pomodoro viewmodel object
class PomodoroViewModel() {
    // Internal time management of pomodoro object
    private var curPomodoro = mutableStateOf<PomodoroModel>(PomodoroModel(5, 25))
    init {
        curPomodoro.value.worktime = 25;
        curPomodoro.value.breaktime = 5;

    }
    // returns current Pomodoro object
    fun getPomodoro(): PomodoroModel {
        return curPomodoro.value;
    }

    // edits current Pomodoro object work time
    fun editWorkTime(workT: Int) {
        curPomodoro.value.worktime = workT;
    }
    // edits current Pomodoro object breakTime
    fun editBreakTime(breakT: Int) {
        curPomodoro.value.breaktime = breakT;
    }

}