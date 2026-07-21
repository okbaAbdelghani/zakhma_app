package com.okbatech.zakhm.presentation.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okbatech.zakhm.domain.model.GoalType
import com.okbatech.zakhm.domain.repository.GoalRepository
import com.okbatech.zakhm.domain.usecase.GetGoalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val getGoals: GetGoalsUseCase,
    private val goalRepository: GoalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GoalsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<GoalsEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            getGoals().collect { goals ->
                _state.update { current ->
                    val filtered = applyFilters(goals, current.selectedFilter, current.showCompleted)
                    current.copy(goals = goals, filteredGoals = filtered, isLoading = false)
                }
            }
        }
    }

    fun onEvent(event: GoalsEvent) {
        when (event) {
            is GoalsEvent.FilterByType -> _state.update { current ->
                val filtered = applyFilters(current.goals, event.type, current.showCompleted)
                current.copy(selectedFilter = event.type, filteredGoals = filtered)
            }
            is GoalsEvent.ToggleShowCompleted -> _state.update { current ->
                val filtered = applyFilters(current.goals, current.selectedFilter, event.show)
                current.copy(showCompleted = event.show, filteredGoals = filtered)
            }
            is GoalsEvent.DeleteGoal -> viewModelScope.launch {
                goalRepository.deleteGoal(event.goalId)
                _effect.emit(GoalsEffect.GoalDeleted)
            }
            is GoalsEvent.NavigateToDetail ->
                viewModelScope.launch { _effect.emit(GoalsEffect.NavigateToDetail(event.goalId)) }
            GoalsEvent.NavigateToAddGoal ->
                viewModelScope.launch { _effect.emit(GoalsEffect.NavigateToAddGoal) }
        }
    }

    private fun applyFilters(goals: List<com.okbatech.zakhm.domain.model.Goal>, type: GoalType?, showCompleted: Boolean) =
        goals.filter { goal ->
            (type == null || goal.type == type) && (showCompleted || !goal.isCompleted)
        }
}
