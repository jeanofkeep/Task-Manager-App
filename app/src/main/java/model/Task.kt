package com.example.test_app.model
//package model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val status: Boolean,
    val date: Long)
