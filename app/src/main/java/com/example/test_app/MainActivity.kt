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
import com.example.test_app.ui.theme.Grey90
import com.example.test_app.ui.theme.White50
import com.example.test_app.ui.theme.Test_appTheme
import com.example.test_app.viewmodel.ThemeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

// Экраны приложения
sealed class Screen(val route: String, val title: String) {
    object Tasks : Screen("tasks", "Tasks")
    object Projects : Screen("projects", "Projects")
    object Shopping : Screen("shopping", "Shopping")
    object Settings : Screen("settings", "Settings")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            Test_appTheme(selectedTheme = themeViewModel.currentTheme) {
                MainScreen(themeViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val items = listOf(Screen.Tasks, Screen.Projects, Screen.Shopping)
    val icons = listOf(Lucide.Check, Lucide.FolderOpen, Lucide.ShoppingCart)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    //val icons = listOf(Icons.Filled.CheckBox, Icons.Filled.Folder, Icons.Filled.ShoppingCart)
    val title = when (currentRoute) {
        Screen.Settings.route -> "Settings"
        else -> "Task Manager v1.0"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route)}) {
                        Icon(Lucide.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
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
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
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
            composable(Screen.Settings.route) { SettingsScreen(themeViewModel) }
        }
    }
}