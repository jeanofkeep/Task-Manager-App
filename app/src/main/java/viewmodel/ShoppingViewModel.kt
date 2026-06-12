//package viewmodel
package com.example.test_app.viewmodel
//package com.example.test_app.model


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.test_app.model.AppDatabase
import com.example.test_app.model.ShoppingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {

    // Подключаемся к базе данных
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "app_database"
    ).fallbackToDestructiveMigration().build()

    private val dao = db.shoppingItemDao()

    // Список задач — UI подписывается на него
    private val _shop_items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val shop_items: StateFlow<List<ShoppingItem>> = _shop_items

    init {
        loadItem_shop()
    }

    // Загрузить все задачи из БД
    private fun loadItem_shop() {
        viewModelScope.launch(Dispatchers.IO) {
            _shop_items.value = dao.getAllTasks()
        }
    }

    // Добавить задачу
    fun addItem_shop(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertItem(
                ShoppingItem(
                    id = 0,
                    name = name,
                    status = false,
                    quantity = 0,
                    dateTime = System.currentTimeMillis()
                )
            )
            loadItem_shop()
        }
    }

    // Удалить задачу
    fun deleteItem_shop(task: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteItem(task)
            loadItem_shop()
        }
    }

    // Сменить статус выполнено/нет
    fun updateItem_shop(task: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateItem(task.copy(status = !task.status))
            loadItem_shop()
        }
    }
}