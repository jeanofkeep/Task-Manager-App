package com.example.test_app
//main_screens
import com.example.test_app.screens.ShoppingScreen
import com.example.test_app.screens.ProjectsScreen
import com.example.test_app.screens.TasksScreen
import com.example.test_app.screens.ProjectDetailScreen
import com.example.test_app.screens.SettingsScreen




import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.FolderOpen
import com.composables.icons.lucide.ShoppingCart
import com.composables.icons.lucide.Settings

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.composables.icons.lucide.Check
import com.example.test_app.ui.theme.Test_appTheme

// Экраны приложения
sealed class Screen(val route: String, val title: String) {
    object Tasks : Screen("tasks", "Задачи")
    object Projects : Screen("projects", "Проекты")
    object Shopping : Screen("shopping", "Покупки")
    object Settings : Screen("settings", "Настройки")
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    //val icons = listOf(Icons.Filled.CheckBox, Icons.Filled.Folder, Icons.Filled.ShoppingCart)
    val title = when (currentRoute) {
        Screen.Settings.route -> "Настройки"
        else -> "Главная"
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) },
            actions = {
                IconButton(onClick = { navController.navigate(Screen.Settings.route)}) {
                    Icon(Lucide.Settings, contentDescription = "Настройки")
                }
            })
        },
        bottomBar = {

            if (currentRoute != Screen.Settings.route) {
                NavigationBar {
                    items.forEachIndexed { index, screen ->
                        NavigationBarItem(
                            icon = { Icon(icons[index], contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            colors = NavigationBarItemDefaults.colors(
                                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            onClick = {
                                if (currentRoute == screen.route) {
                                    navController.popBackStack(screen.route, false)
                                } else {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
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

            composable(Screen.Shopping.route) { ShoppingScreen() }

            composable(Screen.Projects.route) { ProjectsScreen(navController = navController) }
            composable(
                route = "project_detail/{projectId}",
                arguments = listOf(navArgument("projectId") { type = NavType.LongType })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0L
                ProjectDetailScreen(projectId = projectId)
            }

            composable(Screen.Settings.route) { SettingsScreen() }
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