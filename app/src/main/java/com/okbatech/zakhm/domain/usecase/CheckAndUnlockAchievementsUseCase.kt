package com.okbatech.zakhm.domain.usecase

import com.okbatech.zakhm.domain.model.Achievements
import com.okbatech.zakhm.domain.model.UserProfile
import com.okbatech.zakhm.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckAndUnlockAchievementsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(profile: UserProfile) {
        val unlocked = userRepository.getUnlockedAchievementIds().first()
        val now = System.currentTimeMillis()

        val toUnlock = mutableListOf<String>()

        fun check(id: String, condition: Boolean) {
            if (condition && id !in unlocked) toUnlock.add(id)
        }

        check("first_goal", profile.totalHabitLogs >= 0 && profile.totalXp >= 25)
        check("streak_7", profile.currentStreak >= 7)
        check("streak_30", profile.currentStreak >= 30)
        check("streak_100", profile.currentStreak >= 100)
        check("logs_10", profile.totalHabitLogs >= 10)
        check("logs_50", profile.totalHabitLogs >= 50)
        check("logs_100", profile.totalHabitLogs >= 100)
        check("goals_1", profile.totalGoalsCompleted >= 1)
        check("goals_5", profile.totalGoalsCompleted >= 5)
        check("level_5", profile.level >= 5)
        check("level_10", profile.level >= 10)

        var xpBonus = 0
        for (id in toUnlock) {
            userRepository.unlockAchievement(id, now)
            xpBonus += Achievements.getById(id)?.xpReward ?: 0
        }

        if (xpBonus > 0) {
            val newXp = profile.totalXp + xpBonus
            userRepository.updateUserProfile(
                profile.copy(
                    totalXp = newXp,
                    level = UserProfile.levelForXp(newXp)
                )
            )
        }
    }
}
