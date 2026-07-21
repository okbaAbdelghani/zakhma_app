package com.okbatech.zakhm.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.okbatech.zakhm.presentation.achievements.AchievementsScreen
import com.okbatech.zakhm.presentation.addgoal.AddGoalScreen
import com.okbatech.zakhm.presentation.dashboard.DashboardScreen
import com.okbatech.zakhm.presentation.goaldetail.GoalDetailScreen
import com.okbatech.zakhm.presentation.goals.GoalsScreen

private data class BottomTab(val route: String, val icon: ImageVector, val label: String)

private val bottomTabs = listOf(
    BottomTab(Screen.Dashboard.route, Icons.Default.Home, "Home"),
    BottomTab(Screen.Goals.route, Icons.Default.List, "Goals"),
    BottomTab(Screen.Achievements.route, Icons.Default.EmojiEvents, "Badges"),
)

@Composable
fun ZakhmNavGraph() {
    val navController = rememberNavController()
    val backstackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backstackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomTabs.map { it.route }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    bottomTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label, style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        onNavigateToGoalDetail = { navController.navigate("goal_detail/$it") },
                        onNavigateToAddGoal = { navController.navigate(Screen.AddGoal.route) }
                    )
                }
                composable(Screen.Goals.route) {
                    GoalsScreen(
                        onNavigateToDetail = { navController.navigate("goal_detail/$it") },
                        onNavigateToAddGoal = { navController.navigate(Screen.AddGoal.route) }
                    )
                }
                composable(Screen.Achievements.route) { AchievementsScreen() }
                composable(Screen.AddGoal.route) { AddGoalScreen(onBack = { navController.popBackStack() }) }
                composable(
                    route = Screen.GoalDetail().route,
                    arguments = listOf(navArgument("goalId") { type = NavType.LongType })
                ) {
                    GoalDetailScreen(onBack = { navController.popBackStack() })
                }
            }
        }
    }
}
