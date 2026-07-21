package com.okbatech.zakhm.domain.model

data class Goal(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val type: GoalType,
    val emoji: String,
    val colorHex: String,
    val targetValue: Float,
    val currentValue: Float = 0f,
    val unit: String,
    val frequencyType: FrequencyType,
    val startDate: Long,
    val endDate: Long? = null,
    val xpReward: Int,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    val progressPercent: Float
        get() = if (targetValue > 0) (currentValue / targetValue).coerceIn(0f, 1f) else 0f

    val isOverdue: Boolean
        get() = endDate != null && !isCompleted && System.currentTimeMillis() > endDate
}
