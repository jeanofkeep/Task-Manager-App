package com.example.test_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project_tasks")
data class ProjectTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val name: String,
    val status: Boolean = false,
    val dateText: String = ""
)
