package com.okbatech.zakhm.presentation.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Goals : Screen("goals")
    data object AddGoal : Screen("add_goal")
    data object Achievements : Screen("achievements")
    data class GoalDetail(val goalId: Long = 0L) : Screen("goal_detail/{goalId}") {
        fun createRoute(id: Long) = "goal_detail/$id"
    }
}
