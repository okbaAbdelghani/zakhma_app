package com.okbatech.zakhm.domain.usecase

import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.model.HabitLog
import com.okbatech.zakhm.domain.model.UserProfile
import com.okbatech.zakhm.domain.notification.NotificationService
import com.okbatech.zakhm.domain.repository.GoalRepository
import com.okbatech.zakhm.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

class LogHabitUseCase @Inject constructor(
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val checkAchievementsUseCase: CheckAndUnlockAchievementsUseCase,
    private val notificationService: NotificationService
) {
    suspend operator fun invoke(goal: Goal, value: Float, note: String = ""): Int {
        val xpEarned = 10
        val now = System.currentTimeMillis()
        val log = HabitLog(goalId = goal.id, value = value, note = note, completedAt = now, xpEarned = xpEarned)
        goalRepository.insertHabitLog(log)

        val newValue = (goal.currentValue + value).coerceAtMost(goal.targetValue)
        val completed = newValue >= goal.targetValue
        val completionBonus = if (completed && !goal.isCompleted) goal.xpReward else 0

        goalRepository.updateGoal(
            goal.copy(currentValue = newValue, isCompleted = completed)
        )

        val profile = userRepository.getUserProfile().first()
        val streak = computeNewStreak(profile, now)
        val streakBonus = when {
            streak == 7 -> 50
            streak == 30 -> 200
            streak == 100 -> 500
            else -> 0
        }
        val totalXpGain = xpEarned + completionBonus + streakBonus
        val newXp = profile.totalXp + totalXpGain
        val newLevel = UserProfile.levelForXp(newXp)
        val updatedProfile = profile.copy(
            totalXp = newXp,
            level = newLevel,
            currentStreak = streak,
            longestStreak = maxOf(profile.longestStreak, streak),
            lastActiveDate = now,
            totalHabitLogs = profile.totalHabitLogs + 1,
            totalGoalsCompleted = if (completed && !goal.isCompleted) profile.totalGoalsCompleted + 1 else profile.totalGoalsCompleted
        )
        userRepository.updateUserProfile(updatedProfile)
        checkAchievementsUseCase(updatedProfile)

        if (newLevel > profile.level) {
            notificationService.showLevelUp(newLevel, updatedProfile.levelTitle)
        }

        return totalXpGain
    }

    private fun computeNewStreak(profile: UserProfile, now: Long): Int {
        val lastActive = profile.lastActiveDate ?: return 1
        val todayStart = startOfDay(now)
        val lastStart = startOfDay(lastActive)
        val diff = todayStart - lastStart
        return when {
            diff == 0L -> profile.currentStreak
            diff == 86_400_000L -> profile.currentStreak + 1
            else -> 1
        }
    }

    private fun startOfDay(ms: Long): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = ms }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
