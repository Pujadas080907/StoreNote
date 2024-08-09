package com.example.storenotes.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.storenotes.Database.NoteDao
import com.example.storenotes.Database.NoteDatabase
import com.example.storenotes.Database.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TaskRepository
    val allTask: LiveData<List<Task>>

    init {
        val dao2 = NoteDatabase.getDatabase(application).getTaskDao()
        repository = TaskRepository(dao2)
        allTask = repository.allTasks
    }
    fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(task)
    }
    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(task)
    }
    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO){
        repository.update(task)
    }
}