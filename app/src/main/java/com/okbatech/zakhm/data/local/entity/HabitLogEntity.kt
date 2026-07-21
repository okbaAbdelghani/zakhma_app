package com.okbatech.zakhm.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.okbatech.zakhm.domain.model.HabitLog

@Entity(
    tableName = "habit_logs",
    foreignKeys = [ForeignKey(
        entity = GoalEntity::class,
        parentColumns = ["id"],
        childColumns = ["goalId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("goalId")]
)
data class HabitLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val goalId: Long,
    val value: Float,
    val note: String,
    val completedAt: Long,
    val xpEarned: Int
) {
    fun toDomain() = HabitLog(
        id = id,
        goalId = goalId,
        value = value,
        note = note,
        completedAt = completedAt,
        xpEarned = xpEarned
    )
}

fun HabitLog.toEntity() = HabitLogEntity(
    id = id,
    goalId = goalId,
    value = value,
    note = note,
    completedAt = completedAt,
    xpEarned = xpEarned
)
