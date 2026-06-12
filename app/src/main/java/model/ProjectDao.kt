

package com.example.test_app.model
import androidx.room.*

@Dao
interface ProjectDao{
    @Query("SELECT * FROM projects")
    fun getAllProjects(): List<Project>

    @Delete
    fun deleteProject(project: Project)

    @Insert
    fun insertProject(project: Project)

    @Update
    fun updateProject(project: Project)
}