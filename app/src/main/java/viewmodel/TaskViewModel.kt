//package viewmodel

package com.example.test_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.test_app.model.AppDatabase
import com.example.test_app.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val task_db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "app_database"
    ).fallbackToDestructiveMigration().build()

    private val task_dao = task_db.taskDao()

    private val _task_list = MutableStateFlow<List<Task>>(emptyList())
    val task_list: StateFlow<List<Task>> = _task_list

    init {
        load_tasks()
    }

    private fun load_tasks() {
        viewModelScope.launch(Dispatchers.IO) {
            _task_list.value = task_dao.getAllTasks()
        }
    }

    fun insert_task(task_name: String, date: Long = System.currentTimeMillis()) {
        viewModelScope.launch(Dispatchers.IO) {
            task_dao.insertTask(
                Task(
                    id = 0,
                    name = task_name,
                    status = false,
                    date = date
                )
            )
            load_tasks()
        }
    }

    fun delete_task(task_item: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            task_dao.deleteTask(task_item)
            load_tasks()
        }
    }

    fun update_task(task_item: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            task_dao.updateTask(task_item.copy(status = !task_item.status))
            load_tasks()
        }
    }
}