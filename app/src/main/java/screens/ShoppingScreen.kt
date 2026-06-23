//package screens

package com.example.test_app.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test_app.viewmodel.ShoppingViewModel
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ShoppingCart

@Composable
fun ShoppingScreen(shoppingViewModel: ShoppingViewModel = viewModel()) {

    val items by shoppingViewModel.shop_items.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item.status,
                            onCheckedChange = { shoppingViewModel.updateItem_shop(item) }
                        )
                        Text(
                            text = item.name,
                            modifier = Modifier.weight(1f),
                            textDecoration = if (item.status) TextDecoration.LineThrough else TextDecoration.None
                        )
                        IconButton(onClick = { shoppingViewModel.deleteItem_shop(item) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Удалить")
                        }
                    }
                }
            }
        }
    }

    // Диалог добавления
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Новый элемент") },
            text = {
                OutlinedTextField(
                    value = newItemName,
                    onValueChange = { newItemName = it },
                    label = { Text("Название") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newItemName.isNotBlank()) {
                        shoppingViewModel.addItem_shop(newItemName)
                        newItemName = ""
                        showDialog = false
                    }
                }) { Text("Добавить") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Отмена") }
            }
        )
    }
}