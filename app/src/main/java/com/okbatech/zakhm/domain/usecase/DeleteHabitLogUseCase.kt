package com.okbatech.zakhm.domain.usecase

import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.model.HabitLog
import com.okbatech.zakhm.domain.model.UserProfile
import com.okbatech.zakhm.domain.repository.GoalRepository
import com.okbatech.zakhm.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteHabitLogUseCase @Inject constructor(
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(log: HabitLog, goal: Goal): Int {
        val newValue = (goal.currentValue - log.value).coerceAtLeast(0f)
        val wasCompleted = goal.isCompleted
        val isNowCompleted = newValue >= goal.targetValue

        goalRepository.updateGoal(
            goal.copy(currentValue = newValue, isCompleted = isNowCompleted)
        )
        goalRepository.deleteHabitLog(log.id)

        // Deduct log XP plus completion bonus if this log triggered goal completion
        val xpToDeduct = log.xpEarned + if (wasCompleted && !isNowCompleted) goal.xpReward else 0

        val profile = userRepository.getUserProfile().first()
        val newXp = maxOf(0, profile.totalXp - xpToDeduct)
        userRepository.updateUserProfile(
            profile.copy(
                totalXp = newXp,
                level = UserProfile.levelForXp(newXp),
                totalHabitLogs = maxOf(0, profile.totalHabitLogs - 1),
                totalGoalsCompleted = if (wasCompleted && !isNowCompleted)
                    maxOf(0, profile.totalGoalsCompleted - 1) else profile.totalGoalsCompleted
            )
        )
        return xpToDeduct
    }
}
