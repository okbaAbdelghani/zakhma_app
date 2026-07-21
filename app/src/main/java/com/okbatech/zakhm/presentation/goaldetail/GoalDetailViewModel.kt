package com.okbatech.zakhm.presentation.goaldetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okbatech.zakhm.domain.repository.GoalRepository
import com.okbatech.zakhm.domain.usecase.DeleteGoalUseCase
import com.okbatech.zakhm.domain.usecase.DeleteHabitLogUseCase
import com.okbatech.zakhm.domain.usecase.GetGoalStatsUseCase
import com.okbatech.zakhm.domain.usecase.LogHabitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val goalRepository: GoalRepository,
    private val getGoalStats: GetGoalStatsUseCase,
    private val logHabit: LogHabitUseCase,
    private val deleteHabitLog: DeleteHabitLogUseCase,
    private val deleteGoal: DeleteGoalUseCase
) : ViewModel() {

    private val goalId: Long = checkNotNull(savedStateHandle["goalId"])

    private val _state = MutableStateFlow(GoalDetailState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<GoalDetailEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                goalRepository.getGoalById(goalId),
                getGoalStats.getLogsForGoal(goalId)
            ) { goal, logs -> Pair(goal, logs) }.collect { (goal, logs) ->
                _state.update { it.copy(goal = goal, logs = logs, isLoading = false) }
            }
        }
    }

    fun onEvent(event: GoalDetailEvent) {
        when (event) {
            GoalDetailEvent.OpenLogDialog ->
                _state.update { it.copy(showLogDialog = true) }

            GoalDetailEvent.DismissLogDialog ->
                _state.update { it.copy(showLogDialog = false, logNote = "", logValue = "1") }

            is GoalDetailEvent.LogValueChanged ->
                _state.update { it.copy(logValue = event.value) }

            is GoalDetailEvent.LogNoteChanged ->
                _state.update { it.copy(logNote = event.note) }

            GoalDetailEvent.ConfirmLog -> confirmLog()

            is GoalDetailEvent.DeleteLog -> viewModelScope.launch {
                val goal = _state.value.goal ?: return@launch
                val xpDeducted = deleteHabitLog(event.log, goal)
                _effect.emit(GoalDetailEffect.XpDeducted(xpDeducted))
            }

            GoalDetailEvent.RequestDeleteGoal ->
                _state.update { it.copy(showDeleteGoalDialog = true) }

            GoalDetailEvent.DismissDeleteGoal ->
                _state.update { it.copy(showDeleteGoalDialog = false) }

            GoalDetailEvent.ConfirmDeleteGoal -> viewModelScope.launch {
                val goal = _state.value.goal ?: return@launch
                val logs = _state.value.logs
                _state.update { it.copy(showDeleteGoalDialog = false) }
                deleteGoal(goal, logs)
                _effect.emit(GoalDetailEffect.GoalDeleted)
                _effect.emit(GoalDetailEffect.NavigateBack)
            }
        }
    }

    private fun confirmLog() {
        val goal = _state.value.goal ?: return
        val value = _state.value.logValue.toFloatOrNull() ?: 1f
        viewModelScope.launch {
            val xp = logHabit(goal, value, _state.value.logNote)
            _state.update { it.copy(showLogDialog = false, logNote = "", logValue = "1") }
            _effect.emit(GoalDetailEffect.XpGained(xp))
        }
    }
}
