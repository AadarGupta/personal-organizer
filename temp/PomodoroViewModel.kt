package sidebar.pomodoro;

import MyHttp
import androidx.compose.runtime.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import sidebar.pomodoro.PomodoroModel
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import sidebar.reminders.ReminderList
import sidebar.todos.ToDoModel


class PomodoroViewModel(currUser: MutableState<String>) {
    var currUser = currUser
    private var pomodoroList = mutableStateListOf<PomodoroModel>()
    private var currModel = mutableStateOf<PomodoroModel>(PomodoroModel(0, 0,0, false))


    fun getPomodoroList() : List<PomodoroModel> {
        return pomodoroList;
    }

    fun isPomodoroEmpty() : Boolean {
        return pomodoroList.isEmpty()
    }



    fun getIdxById(pomodoroItem: PomodoroModel): Int {
        return pomodoroList.indexOf(pomodoroItem)
    }

    fun getItemByIdx(idx: Int): PomodoroModel {
        return pomodoroList[idx]
    }
}
