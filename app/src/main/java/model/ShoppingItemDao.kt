package com.example.test_app.model
import androidx.room.*

@Dao
interface ShoppingItemDao{
    @Query("SELECT * FROM shopping")
    fun getAllTasks(): List<ShoppingItem>

    @Insert
    fun insertItem(shopping: ShoppingItem)

    @Delete
    fun deleteItem(shopping: ShoppingItem)

    @Update
    fun updateItem(shopping: ShoppingItem)
}