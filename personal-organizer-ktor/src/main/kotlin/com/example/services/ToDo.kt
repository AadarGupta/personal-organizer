package com.example.services

import com.example.models.db.ToDoDbModel
import com.example.models.db.ToDoDbObject
import com.example.models.http.ToDoItem
import com.example.models.http.ToDoListResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


fun createToDo(
    itemOwner: String,
    name: String,
    checkedStatus: Boolean
) : ToDoDbModel {
    var targetToDoItem : ToDoDbModel =
        ToDoDbModel(
            0,
            "",
            "",
            true
        );
    transaction {
        val newToDo = ToDoDbObject.insert {
            it[owner] = itemOwner
            it[itemName] = name
            it[isChecked] = checkedStatus
        } get ToDoDbObject.id

        targetToDoItem =
            ToDoDbModel(
                newToDo.value,
                itemOwner,
                name,
                checkedStatus
            )

    }
    return targetToDoItem
}

fun editToDoName(
    itemOwner: String,
    id: Int,
    name: String
) {
    transaction {
        ToDoDbObject.update({ToDoDbObject.id.eq(id) and ToDoDbObject.owner.eq(itemOwner)}) {
            it[itemName] = name
        }
    }
}

fun editToDoChecked(
    itemOwner: String,
    id: Int,
    checkedStatus: Boolean
) {
    transaction {
        ToDoDbObject.update({ToDoDbObject.id.eq(id) and ToDoDbObject.owner.eq(itemOwner)}) {
            it[isChecked] = checkedStatus
        }
    }
}


fun deleteToDo(itemOwner: String, id: Int) {
    transaction {
        ToDoDbObject.deleteWhere { ToDoDbObject.id.eq(id) and ToDoDbObject.owner.eq(itemOwner) }
    }
}


fun getAllToDos(itemOwner: String) : ToDoListResponse {
    val toDoList = mutableListOf<ToDoItem>()
    transaction {
        for (todo in ToDoDbObject.selectAll()) {
            if (todo[ToDoDbObject.owner] == itemOwner) {
                val toDoData = ToDoItem(
                    todo[ToDoDbObject.id].value,
                    todo[ToDoDbObject.owner],
                    todo[ToDoDbObject.itemName],
                    todo[ToDoDbObject.isChecked]
                )
                toDoList.add(toDoData)
            }
        }
    }
    val toDoListResponse = ToDoListResponse(toDoList)
    return toDoListResponse
}
