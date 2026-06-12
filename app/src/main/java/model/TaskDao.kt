package com.example.test_app.model
//package model
import androidx.room.*

@Dao
interface TaskDao{
    @Query("SELECT * FROM task")
    fun getAllTasks(): List<Task>

    @Insert
    fun insertTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)
}