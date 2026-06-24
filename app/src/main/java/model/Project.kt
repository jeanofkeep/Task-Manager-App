// model

package com.example.test_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val status: Boolean,
    val dateTime: Long,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val dueDateText: String = ""
)

//id, name, isBought, quantity, dateTime.

