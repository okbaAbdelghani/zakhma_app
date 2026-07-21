package com.okbatech.zakhm.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okbatech.zakhm.domain.usecase.GetGoalsUseCase
import com.okbatech.zakhm.domain.usecase.GetUserProfileUseCase
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
class DashboardViewModel @Inject constructor(
    private val getGoals: GetGoalsUseCase,
    private val getUserProfile: GetUserProfileUseCase,
    private val logHabit: LogHabitUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DashboardEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(getUserProfile(), getGoals()) { profile, goals ->
                val active = goals.filter { !it.isCompleted }
                DashboardState(
                    profile = profile,
                    activeGoals = active,
                    todayGoals = active.take(3),
                    isLoading = false
                )
            }.collect { newState ->
                _state.update { newState }
            }
        }
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.LogGoal -> viewModelScope.launch {
                val xp = logHabit(event.goal, 1f)
                _effect.emit(DashboardEffect.ShowXpGain(xp))
            }
            is DashboardEvent.NavigateToGoalDetail ->
                viewModelScope.launch { _effect.emit(DashboardEffect.NavigateToGoalDetail(event.goalId)) }
            DashboardEvent.NavigateToAddGoal ->
                viewModelScope.launch { _effect.emit(DashboardEffect.NavigateToAddGoal) }
        }
    }
}
