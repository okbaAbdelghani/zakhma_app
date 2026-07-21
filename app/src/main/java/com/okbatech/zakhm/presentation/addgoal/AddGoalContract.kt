package com.okbatech.zakhm.presentation.addgoal

import com.okbatech.zakhm.domain.model.FrequencyType
import com.okbatech.zakhm.domain.model.GoalType

data class AddGoalState(
    val title: String = "",
    val description: String = "",
    val type: GoalType = GoalType.HABIT,
    val emoji: String = "🎯",
    val colorHex: String = "#00E676",
    val targetValue: String = "1",
    val unit: String = "times",
    val frequencyType: FrequencyType = FrequencyType.DAILY,
    val hasDeadline: Boolean = false,
    val deadlineMs: Long? = null,
    val isLoading: Boolean = false,
    val titleError: String? = null,
    val targetError: String? = null
)

sealed class AddGoalEvent {
    data class TitleChanged(val value: String) : AddGoalEvent()
    data class DescriptionChanged(val value: String) : AddGoalEvent()
    data class TypeSelected(val type: GoalType) : AddGoalEvent()
    data class EmojiSelected(val emoji: String) : AddGoalEvent()
    data class ColorSelected(val hex: String) : AddGoalEvent()
    data class TargetValueChanged(val value: String) : AddGoalEvent()
    data class UnitChanged(val unit: String) : AddGoalEvent()
    data class FrequencySelected(val frequency: FrequencyType) : AddGoalEvent()
    data class DeadlineToggled(val enabled: Boolean) : AddGoalEvent()
    data class DeadlineChanged(val ms: Long) : AddGoalEvent()
    data object Submit : AddGoalEvent()
}

sealed class AddGoalEffect {
    data object GoalCreated : AddGoalEffect()
    data object NavigateBack : AddGoalEffect()
}
