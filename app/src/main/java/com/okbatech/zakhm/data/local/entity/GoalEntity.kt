package com.okbatech.zakhm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.okbatech.zakhm.domain.model.FrequencyType
import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.model.GoalType

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val type: String,
    val emoji: String,
    val colorHex: String,
    val targetValue: Float,
    val currentValue: Float,
    val unit: String,
    val frequencyType: String,
    val startDate: Long,
    val endDate: Long?,
    val xpReward: Int,
    val isCompleted: Boolean,
    val createdAt: Long
) {
    fun toDomain() = Goal(
        id = id,
        title = title,
        description = description,
        type = GoalType.valueOf(type),
        emoji = emoji,
        colorHex = colorHex,
        targetValue = targetValue,
        currentValue = currentValue,
        unit = unit,
        frequencyType = FrequencyType.valueOf(frequencyType),
        startDate = startDate,
        endDate = endDate,
        xpReward = xpReward,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}

fun Goal.toEntity() = GoalEntity(
    id = id,
    title = title,
    description = description,
    type = type.name,
    emoji = emoji,
    colorHex = colorHex,
    targetValue = targetValue,
    currentValue = currentValue,
    unit = unit,
    frequencyType = frequencyType.name,
    startDate = startDate,
    endDate = endDate,
    xpReward = xpReward,
    isCompleted = isCompleted,
    createdAt = createdAt
)
