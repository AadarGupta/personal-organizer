package com.example.services

import com.example.models.db.PomodoroDbModel
import com.example.models.db.PomodoroDbObject
import com.example.models.http.PomodoroItem
import com.example.models.http.PomodoroListResponse
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


fun createPomodoro(btime: Int, wtime: Int, checkedStatus: Boolean) : PomodoroDbModel {
    var targetPomodoroItem : PomodoroDbModel = PomodoroDbModel(0, 0,0, true);
    transaction {
        val newPomodoro = PomodoroDbObject.insert {
            it[breaktime] = btime
            it[worktime] = wtime
            it[isChecked] = checkedStatus
        } get PomodoroDbObject.id
        println(newPomodoro.value)
        targetPomodoroItem = PomodoroDbModel(newPomodoro.value, btime, wtime, checkedStatus)
        println(newPomodoro)

    }
    return targetPomodoroItem
}



fun editPomodoroChecked(id: Int, checkedStatus: Boolean) {
    transaction {
        PomodoroDbObject.update({PomodoroDbObject.id eq id}) {
            it[isChecked] = checkedStatus
        }
    }
}



fun getAllPomodoros() : PomodoroListResponse {
    val pomodoroList = mutableListOf<PomodoroItem>()
    transaction {
        for (pomodoro in PomodoroDbObject.selectAll()) {
            val pomodoroData = PomodoroItem(pomodoro[PomodoroDbObject.id].value, pomodoro[PomodoroDbObject.breaktime], pomodoro[PomodoroDbObject.worktime], pomodoro[PomodoroDbObject.isChecked])
            pomodoroList.add(pomodoroData)
        }
    }
    val pomodoroListResponse = PomodoroListResponse(pomodoroList)
    return pomodoroListResponse
}
