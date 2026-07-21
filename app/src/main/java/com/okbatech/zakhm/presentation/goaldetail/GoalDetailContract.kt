package com.okbatech.zakhm.presentation.goaldetail

import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.model.HabitLog

data class GoalDetailState(
    val goal: Goal? = null,
    val logs: List<HabitLog> = emptyList(),
    val isLoading: Boolean = true,
    val showLogDialog: Boolean = false,
    val showDeleteGoalDialog: Boolean = false,
    val logNote: String = "",
    val logValue: String = "1"
)

sealed class GoalDetailEvent {
    data object OpenLogDialog : GoalDetailEvent()
    data object DismissLogDialog : GoalDetailEvent()
    data class LogValueChanged(val value: String) : GoalDetailEvent()
    data class LogNoteChanged(val note: String) : GoalDetailEvent()
    data object ConfirmLog : GoalDetailEvent()
    data class DeleteLog(val log: HabitLog) : GoalDetailEvent()
    data object RequestDeleteGoal : GoalDetailEvent()
    data object ConfirmDeleteGoal : GoalDetailEvent()
    data object DismissDeleteGoal : GoalDetailEvent()
}

sealed class GoalDetailEffect {
    data class XpGained(val xp: Int) : GoalDetailEffect()
    data class XpDeducted(val xp: Int) : GoalDetailEffect()
    data object GoalDeleted : GoalDetailEffect()
    data object NavigateBack : GoalDetailEffect()
}
