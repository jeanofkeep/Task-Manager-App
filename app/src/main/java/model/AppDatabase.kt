package com.example.test_app.model
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class, ShoppingItem::class, Project::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao
    abstract fun projectDao() : ProjectDao
    abstract fun shoppingItemDao(): ShoppingItemDao
}