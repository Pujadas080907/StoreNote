package com.example.storenotes.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storenotes.Models.Note
import com.example.storenotes.Models.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)

//    @Query("Select * from tasks_table order by id ASC")
//    fun getAllTasks(): LiveData<List<Task>>
//
////    @Query("UPDATE tasks_table Set title = title, task = task WHERE id = :id")
////    suspend fun update(id:Int?, title:String?, task: String?)
//    @Query("UPDATE tasks_table SET title = :title, task = :task WHERE id = :id")
//    suspend fun update(id: Int?, title: String?, task: String?)

    @Query("SELECT * FROM tasks_table ORDER BY id ASC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("UPDATE tasks_table SET title = :title, task = :task WHERE id = :id")
    suspend fun update(id: Int?, title: String?, task: String?)

}