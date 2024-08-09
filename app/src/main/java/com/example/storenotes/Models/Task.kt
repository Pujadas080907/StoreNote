package com.example.storenotes.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//data class Task(
//    val title:String,
//    val subtasks: List<Subtask>
//)

@Entity(tableName = "tasks_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "task") val task: String?,
) : Serializable
