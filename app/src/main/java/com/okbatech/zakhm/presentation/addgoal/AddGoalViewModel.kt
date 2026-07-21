package com.okbatech.zakhm.presentation.addgoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okbatech.zakhm.domain.model.FrequencyType
import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.model.GoalType
import com.okbatech.zakhm.domain.usecase.CreateGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGoalViewModel @Inject constructor(
    private val createGoal: CreateGoalUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddGoalState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddGoalEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: AddGoalEvent) {
        when (event) {
            is AddGoalEvent.TitleChanged -> _state.update { it.copy(title = event.value, titleError = null) }
            is AddGoalEvent.DescriptionChanged -> _state.update { it.copy(description = event.value) }
            is AddGoalEvent.TypeSelected -> _state.update {
                it.copy(
                    type = event.type,
                    emoji = event.type.emoji,
                    unit = defaultUnit(event.type),
                    colorHex = defaultColor(event.type)
                )
            }
            is AddGoalEvent.EmojiSelected -> _state.update { it.copy(emoji = event.emoji) }
            is AddGoalEvent.ColorSelected -> _state.update { it.copy(colorHex = event.hex) }
            is AddGoalEvent.TargetValueChanged -> _state.update { it.copy(targetValue = event.value, targetError = null) }
            is AddGoalEvent.UnitChanged -> _state.update { it.copy(unit = event.unit) }
            is AddGoalEvent.FrequencySelected -> _state.update { it.copy(frequencyType = event.frequency) }
            is AddGoalEvent.DeadlineToggled -> _state.update { it.copy(hasDeadline = event.enabled, deadlineMs = null) }
            is AddGoalEvent.DeadlineChanged -> _state.update { it.copy(deadlineMs = event.ms) }
            AddGoalEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val s = _state.value
        var valid = true
        if (s.title.isBlank()) {
            _state.update { it.copy(titleError = "Title is required") }
            valid = false
        }
        val target = s.targetValue.toFloatOrNull()
        if (target == null || target <= 0) {
            _state.update { it.copy(targetError = "Enter a valid target") }
            valid = false
        }
        if (!valid) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            createGoal(
                Goal(
                    title = s.title.trim(),
                    description = s.description.trim(),
                    type = s.type,
                    emoji = s.emoji,
                    colorHex = s.colorHex,
                    targetValue = target!!,
                    unit = s.unit,
                    frequencyType = s.frequencyType,
                    startDate = System.currentTimeMillis(),
                    endDate = if (s.hasDeadline) s.deadlineMs else null,
                    xpReward = xpRewardFor(s.type)
                )
            )
            _effect.emit(AddGoalEffect.GoalCreated)
        }
    }

    private fun defaultUnit(type: GoalType) = when (type) {
        GoalType.FINANCIAL -> "$"
        GoalType.FITNESS -> "km"
        GoalType.HABIT -> "times"
        GoalType.LEARNING -> "pages"
        GoalType.MINDFULNESS -> "min"
        GoalType.CUSTOM -> "times"
    }

    private fun defaultColor(type: GoalType) = when (type) {
        GoalType.FINANCIAL -> "#00E676"
        GoalType.FITNESS -> "#40C4FF"
        GoalType.HABIT -> "#26C6DA"
        GoalType.LEARNING -> "#FFAB40"
        GoalType.MINDFULNESS -> "#64FFDA"
        GoalType.CUSTOM -> "#FF4081"
    }

    private fun xpRewardFor(type: GoalType) = when (type) {
        GoalType.FINANCIAL -> 300
        GoalType.FITNESS -> 200
        GoalType.HABIT -> 150
        GoalType.LEARNING -> 200
        GoalType.MINDFULNESS -> 150
        GoalType.CUSTOM -> 100
    }
}
