package com.example.services

import com.example.models.db.ToDoDbModel
import com.example.models.db.ToDoDbObject
import com.example.models.http.ToDoItem
import com.example.models.http.ToDoListResponse
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


fun createToDo(name: String, checkedStatus: Boolean) : ToDoDbModel {
    var targetToDoItem : ToDoDbModel = ToDoDbModel(0, "null", true);
    transaction {
        val newToDo = ToDoDbObject.insert {
            it[itemName] = name
            it[isChecked] = checkedStatus
        } get ToDoDbObject.id
        println(newToDo.value)
        targetToDoItem = ToDoDbModel(newToDo.value, name, checkedStatus)
        println(newToDo)

    }
    return targetToDoItem
}

fun editToDoName(id: Int, name: String) {
    transaction {
        ToDoDbObject.update({ToDoDbObject.id eq id}) {
            it[itemName] = name
        }
    }
}

fun editToDoChecked(id: Int, checkedStatus: Boolean) {
    transaction {
        ToDoDbObject.update({ToDoDbObject.id eq id}) {
            it[isChecked] = checkedStatus
        }
    }
}


fun deleteToDo(id: Int) {
    transaction {
        ToDoDbObject.deleteWhere { ToDoDbObject.id eq id }
    }
}


fun getAllToDos() : ToDoListResponse {
    val toDoList = mutableListOf<ToDoItem>()
    transaction {
        for (todo in ToDoDbObject.selectAll()) {
            val toDoData = ToDoItem(todo[ToDoDbObject.id].value, todo[ToDoDbObject.itemName], todo[ToDoDbObject.isChecked])
            toDoList.add(toDoData)
        }
    }
    val toDoListResponse = ToDoListResponse(toDoList)
    return toDoListResponse
}