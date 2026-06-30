package com.example.test_app.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.test_app.model.ProjectTask
import com.example.test_app.ui.theme.icon_colors
import com.example.test_app.viewmodel.ProjectViewModel
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Briefcase
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.CirclePlus
import com.example.test_app.ui.theme.White30
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(navController: NavController, projectViewModel: ProjectViewModel = viewModel()) {

    val project_list by projectViewModel.project_items.collectAsState()
    val all_tasks by projectViewModel.project_tasks.collectAsState()
    
    var showProjectDialog by remember { mutableStateOf(false) }
    var newProjectName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    var showTaskDialog by remember { mutableStateOf(false) }
    var newTaskName by remember { mutableStateOf("") }
    var targetProjectId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                selectedDate = LocalDate.now()
                showProjectDialog = true 
            }) {
                Icon(Lucide.Plus, contentDescription = "Add project")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            itemsIndexed(project_list) { index, project ->
                val color = icon_colors[index % icon_colors.size]
                val projectTasks = all_tasks[project.id] ?: emptyList()
                val progress = if (project.totalTasks > 0)
                    project.completedTasks.toFloat() / project.totalTasks.toFloat()
                else 0f

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Header
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(color.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Lucide.Briefcase, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(project.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(
                                    text = "${project.completedTasks} из ${project.totalTasks} задач · до ${project.dueDateText}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            Box(modifier = Modifier.width(60.dp)) {
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp)),
                                    color = color,
                                    trackColor = color.copy(alpha = 0.1f)
                                )
                            }
                            IconButton(onClick = { projectViewModel.deleteProject(project) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete", modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                        // Tasks List
                        projectTasks.forEach { task ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = task.status,
                                    onCheckedChange = { projectViewModel.update_task(task) },
                                    colors = CheckboxDefaults.colors(checkedColor = color)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = task.name,
                                        fontSize = 14.sp,
                                        textDecoration = if (task.status) TextDecoration.LineThrough else TextDecoration.None,
                                        color = if (task.status) Color.Gray else Color.Unspecified
                                    )
                                    Text(task.dateText, style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                                }
                                IconButton(onClick = { projectViewModel.delete_task(task) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = White30, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        // Add Task Button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    targetProjectId = project.id
                                    showTaskDialog = true
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Lucide.CirclePlus, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Add task", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
    // Dialog for adding project
    if (showProjectDialog) {
        AlertDialog(
            onDismissRequest = { showProjectDialog = false },
            title = { Text("New project") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newProjectName,
                        onValueChange = { newProjectName = it },
                        label = { Text("Name of project") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Срок: ${dateFormatter.format(selectedDate)}")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newProjectName.isNotBlank()) {
                        projectViewModel.insert_project(newProjectName, 0, dateFormatter.format(selectedDate))
                        newProjectName = ""
                        showProjectDialog = false
                    }
                }) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showProjectDialog = false }) { Text("Cancel") }
            }
        )
    }
    // Dialog for adding task to project
    if (showTaskDialog) {
        AlertDialog(
            onDismissRequest = { showTaskDialog = false },
            title = { Text("Task for project") },
            text = {
                OutlinedTextField(
                    value = newTaskName,
                    onValueChange = { newTaskName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newTaskName.isNotBlank() && targetProjectId != null) {
                        projectViewModel.insert_task(targetProjectId!!, newTaskName, dateFormatter.format(LocalDate.now()))
                        newTaskName = ""
                        showTaskDialog = false
                    }
                }) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showTaskDialog = false }) { Text("Cancel") }
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
                        selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}