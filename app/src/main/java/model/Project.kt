// model

package com.example.test_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val status: Boolean,
    val dateTime: Long)

//id, name, isBought, quantity, dateTime.