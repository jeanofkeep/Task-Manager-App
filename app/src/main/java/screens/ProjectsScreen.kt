package com.example.test_app.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.test_app.viewmodel.ProjectViewModel
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Briefcase
import com.composables.icons.lucide.EllipsisVertical
import com.composables.icons.lucide.Plus
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val project_colors = listOf(
    Color(0xFF8B7FD1),
    Color(0xFFE8965A),
    Color(0xFF5BA3D9),
    Color(0xFFC2548C)
)

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(navController: NavController, projectViewModel: ProjectViewModel = viewModel()) {

    val project_list by projectViewModel.project_items.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newProjectName by remember { mutableStateOf("") }
    
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                selectedDate = LocalDate.now()
                selectedTime = LocalTime.now()
                showDialog = true 
            }) {
                Icon(Lucide.Plus, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            itemsIndexed(project_list) { index, project ->
                val color = project_colors[index % project_colors.size]
                val icon = Lucide.Briefcase // Всегда используем сундучок
                val progress = if (project.totalTasks > 0)
                    project.completedTasks.toFloat() / project.totalTasks.toFloat()
                else 0f
                val percentText = (progress * 100).toInt()
                val isDone = project.totalTasks > 0 && project.completedTasks >= project.totalTasks

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { navController.navigate("project_detail/${project.id}") },
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(color.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, contentDescription = null, tint = color)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(project.name, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "До ${project.dueDateText}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            IconButton(onClick = { projectViewModel.deleteProject(project) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Удалить")
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = color,
                                trackColor = color.copy(alpha = 0.15f)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "$percentText%",
                                color = color,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (isDone) {
                            Spacer(Modifier.height(6.dp))
                            AssistChip(onClick = {}, label = { Text("Готово ✓") })
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Новый проект") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newProjectName,
                        onValueChange = { newProjectName = it },
                        label = { Text("Название") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Дата: ${dateFormatter.format(selectedDate)}")
                    }
                    OutlinedButton(
                        onClick = { showTimePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Время: ${timeFormatter.format(selectedTime)}")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newProjectName.isNotBlank()) {
                        val formattedDate = dateFormatter.format(selectedDate)
                        projectViewModel.insert_project(
                            newProjectName,
                            0, // По умолчанию 0 задач, так как поле ввода удалено
                            formattedDate
                        )
                        newProjectName = ""
                        showDialog = false
                    }
                }) { Text("Добавить") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Отмена") }
            }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedTime.hour,
            initialMinute = selectedTime.minute,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Выберите время") },
            confirmButton = {
                TextButton(onClick = {
                    selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Отмена") }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}
