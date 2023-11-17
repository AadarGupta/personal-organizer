package sidebar.pomodoro;

import MyHttp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import sidebar.pomodoro.PomodoroList
import sidebar.pomodoro.PomodoroModel
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import androidx.compose.runtime.LaunchedEffect

@Serializable
data class PomodoroList(val items: List<PomodoroModel>)

class PomodoroViewModel() {
    private var pomodoroList = mutableStateListOf<PomodoroModel>()
    private var currModel = mutableStateOf<PomodoroModel>(PomodoroModel(0, 0,0, false))

    init {
        val http = MyHttp()
        val getAllPomodoroResponse : String = http.get("pomodoro")
        val obj = Json.decodeFromString<PomodoroList>(getAllPomodoroResponse)

        for (pomodoro in obj.items) {
            pomodoroList.add(pomodoro)
        }

    }

    fun getPomodoroList() : List<PomodoroModel> {
        return pomodoroList;
    }

    fun isPomodoroEmpty() : Boolean {
        return pomodoroList.isEmpty()
    }


    fun addPomodoro(breaktime: Int,worktime: Int) : Int {
        val http = MyHttp()
        val body = JsonObject(
            mapOf(
                "breaktime" to JsonPrimitive(breaktime),
                "worktime" to JsonPrimitive(worktime),
                "isChecked" to JsonPrimitive(false)
            )
        )
        val createPomodoroResponse = http.post("pomodoro", body)
        val newItem = Json.decodeFromString<PomodoroModel>(createPomodoroResponse)
        pomodoroList.add(newItem)

        return pomodoroList.size - 1
    }

        fun changePomodoroCheckStatus(targetItem: PomodoroModel) {
        val idx = pomodoroList.indexOf(targetItem)
        pomodoroList[idx] = PomodoroModel(pomodoroList[idx].id, pomodoroList[idx].breaktime, pomodoroList[idx].worktime, true)
        val http = MyHttp()
        for (p in pomodoroList){
            if(p.isChecked){
                val body = JsonObject(
                    mapOf(
                        "id" to JsonPrimitive(p.id),
                        "isChecked" to JsonPrimitive((false).toString())
                    )
                )
                http.put("pomodoro/checked", body)
            }
        }
        val body = JsonObject(
            mapOf(
                "id" to JsonPrimitive(targetItem.id),
                "isChecked" to JsonPrimitive((true).toString())
            )
        )
        http.put("pomodoro/checked", body)
        currModel.value = targetItem
    }


    fun getIdxById(pomodoroItem: PomodoroModel): Int {
        return pomodoroList.indexOf(pomodoroItem)
    }

    fun getItemByIdx(idx: Int): PomodoroModel {
        return pomodoroList[idx]
    }
}
