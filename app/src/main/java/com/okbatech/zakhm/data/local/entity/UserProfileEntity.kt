package com.okbatech.zakhm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.okbatech.zakhm.domain.model.UserProfile

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val totalXp: Int,
    val level: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val lastActiveDate: Long?,
    val totalGoalsCompleted: Int,
    val totalHabitLogs: Int
) {
    fun toDomain() = UserProfile(
        id = id,
        totalXp = totalXp,
        level = level,
        currentStreak = currentStreak,
        longestStreak = longestStreak,
        lastActiveDate = lastActiveDate,
        totalGoalsCompleted = totalGoalsCompleted,
        totalHabitLogs = totalHabitLogs
    )
}

fun UserProfile.toEntity() = UserProfileEntity(
    id = id,
    totalXp = totalXp,
    level = level,
    currentStreak = currentStreak,
    longestStreak = longestStreak,
    lastActiveDate = lastActiveDate,
    totalGoalsCompleted = totalGoalsCompleted,
    totalHabitLogs = totalHabitLogs
)
