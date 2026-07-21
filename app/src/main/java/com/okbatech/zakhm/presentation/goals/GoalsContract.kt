package com.okbatech.zakhm.presentation.goals

import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.model.GoalType

data class GoalsState(
    val goals: List<Goal> = emptyList(),
    val filteredGoals: List<Goal> = emptyList(),
    val selectedFilter: GoalType? = null,
    val showCompleted: Boolean = false,
    val isLoading: Boolean = true
)

sealed class GoalsEvent {
    data class FilterByType(val type: GoalType?) : GoalsEvent()
    data class ToggleShowCompleted(val show: Boolean) : GoalsEvent()
    data class DeleteGoal(val goalId: Long) : GoalsEvent()
    data class NavigateToDetail(val goalId: Long) : GoalsEvent()
    data object NavigateToAddGoal : GoalsEvent()
}

sealed class GoalsEffect {
    data class NavigateToDetail(val goalId: Long) : GoalsEffect()
    data object NavigateToAddGoal : GoalsEffect()
    data object GoalDeleted : GoalsEffect()
}
