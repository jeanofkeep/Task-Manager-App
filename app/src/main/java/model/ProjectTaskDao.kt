package com.example.test_app.model

import androidx.room.*

@Dao
interface ProjectTaskDao {
    @Query("SELECT * FROM project_tasks")
    fun getAllProjectTasks(): List<ProjectTask>

    @Query("SELECT * FROM project_tasks WHERE projectId = :projectId")
    fun getTasksForProject(projectId: Long): List<ProjectTask>

    @Insert
    fun insertTask(task: ProjectTask)

    @Update
    fun updateTask(task: ProjectTask)

    @Delete
    fun deleteTask(task: ProjectTask)
}
