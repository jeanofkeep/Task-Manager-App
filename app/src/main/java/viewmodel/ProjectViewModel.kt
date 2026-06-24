package com.example.test_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.test_app.model.AppDatabase
import com.example.test_app.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "app_database"
    ).fallbackToDestructiveMigration().build()

    private val dao = db.projectDao()

    private val _project_items = MutableStateFlow<List<Project>>(emptyList())
    val project_items: StateFlow<List<Project>> = _project_items

    init {
        load_projects()
    }

    private fun load_projects() {
        viewModelScope.launch(Dispatchers.IO) {
            _project_items.value = dao.getAllProjects()
        }
    }

    fun insert_project(name: String, totalTasks: Int, dueDateText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertProject(
                Project(
                    id = 0,
                    name = name,
                    status = false,
                    dateTime = System.currentTimeMillis(),
                    totalTasks = totalTasks,
                    completedTasks = 0,
                    dueDateText = dueDateText
                )
            )
            load_projects()
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteProject(project)
            load_projects()
        }
    }

    fun updateProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateProject(project.copy(status = !project.status))
            load_projects()
        }
    }
}