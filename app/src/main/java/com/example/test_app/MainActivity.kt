package com.example.test_app
//main_screens
import com.example.test_app.screens.ShoppingScreen
import com.example.test_app.screens.ProjectsScreen
import com.example.test_app.screens.TasksScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.FolderOpen
import com.composables.icons.lucide.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.Check
import com.example.test_app.ui.theme.Test_appTheme

// Экраны приложения
sealed class Screen(val route: String, val title: String) {
    object Tasks : Screen("tasks", "Задачи")
    object Projects : Screen("projects", "Проекты")
    object Shopping : Screen("shopping", "Покупки")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Test_appTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(Screen.Tasks, Screen.Projects, Screen.Shopping)
    val icons = listOf(Lucide.Check, Lucide.FolderOpen, Lucide.ShoppingCart)
    //val icons = listOf(Icons.Filled.CheckBox, Icons.Filled.Folder, Icons.Filled.ShoppingCart)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Главная") })
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Tasks.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Tasks.route) { TasksScreen() }
            composable(Screen.Projects.route) { ProjectsScreen() }
            composable(Screen.Shopping.route) { ShoppingScreen() }
        }
    }
}

// Заглушки экранов
//@Composable
//fun TasksScreen() {
  //  Text("Задачи")
//}

//@Composable
//fun ProjectsScreen() {
  //  Text("Проекты")
//}

//@Composable
//fun ShoppingScreen() {
  //  Text("Покупки")
//}