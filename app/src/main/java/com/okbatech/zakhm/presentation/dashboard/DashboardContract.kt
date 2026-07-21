package com.okbatech.zakhm.presentation.dashboard

import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.model.UserProfile

data class DashboardState(
    val profile: UserProfile = UserProfile(),
    val todayGoals: List<Goal> = emptyList(),
    val activeGoals: List<Goal> = emptyList(),
    val isLoading: Boolean = true,
    val xpGainedToday: Int = 0
)

sealed class DashboardEvent {
    data class LogGoal(val goal: Goal) : DashboardEvent()
    data class NavigateToGoalDetail(val goalId: Long) : DashboardEvent()
    data object NavigateToAddGoal : DashboardEvent()
}

sealed class DashboardEffect {
    data class NavigateToGoalDetail(val goalId: Long) : DashboardEffect()
    data object NavigateToAddGoal : DashboardEffect()
    data class ShowXpGain(val xp: Int) : DashboardEffect()
}
