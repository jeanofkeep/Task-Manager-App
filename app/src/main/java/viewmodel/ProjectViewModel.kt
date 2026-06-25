package com.example.test_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.test_app.model.AppDatabase
import com.example.test_app.model.Project
import com.example.test_app.model.ProjectTask
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

    private val projectDao = db.projectDao()
    private val taskDao = db.projectTaskDao()

    private val _project_items = MutableStateFlow<List<Project>>(emptyList())
    val project_items: StateFlow<List<Project>> = _project_items

    private val _project_tasks = MutableStateFlow<Map<Long, List<ProjectTask>>>(emptyMap())
    val project_tasks: StateFlow<Map<Long, List<ProjectTask>>> = _project_tasks

    init {
        load_projects()
        load_all_tasks()
    }

    private fun load_projects() {
        viewModelScope.launch(Dispatchers.IO) {
            _project_items.value = projectDao.getAllProjects()
        }
    }

    private fun load_all_tasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val allTasks = taskDao.getAllProjectTasks()
            _project_tasks.value = allTasks.groupBy { it.projectId }
        }
    }

    fun insert_project(name: String, totalTasks: Int, dueDateText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            projectDao.insertProject(
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
            projectDao.deleteProject(project)
            load_projects()
        }
    }

    fun insert_task(projectId: Long, name: String, dateText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insertTask(ProjectTask(projectId = projectId, name = name, dateText = dateText))
            load_all_tasks()
            updateProjectProgress(projectId)
        }
    }

    fun update_task(task: ProjectTask) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.updateTask(task.copy(status = !task.status))
            load_all_tasks()
            updateProjectProgress(task.projectId)
        }
    }

    fun delete_task(task: ProjectTask) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.deleteTask(task)
            load_all_tasks()
            updateProjectProgress(task.projectId)
        }
    }

    private fun updateProjectProgress(projectId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val tasks = taskDao.getTasksForProject(projectId)
            val total = tasks.size
            val completed = tasks.count { it.status }
            val project = projectDao.getAllProjects().find { it.id == projectId }
            project?.let {
                projectDao.updateProject(it.copy(totalTasks = total, completedTasks = completed))
                load_projects()
            }
        }
    }
}
