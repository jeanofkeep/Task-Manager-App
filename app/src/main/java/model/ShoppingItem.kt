package com.example.test_app.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val status: Boolean,
    val quantity: Long,
    val dateTime: Long)

//id, name, isBought, quantity, dateTime.