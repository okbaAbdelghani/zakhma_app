package com.okbatech.zakhm.domain.usecase

import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.model.HabitLog
import com.okbatech.zakhm.domain.model.UserProfile
import com.okbatech.zakhm.domain.repository.GoalRepository
import com.okbatech.zakhm.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteGoalUseCase @Inject constructor(
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(goal: Goal, logs: List<HabitLog>) {
        // Collect all XP that came from this goal
        val logsXp = logs.sumOf { it.xpEarned }
        val completionXp = if (goal.isCompleted) goal.xpReward else 0
        val creationXp = 25 // awarded when goal was created
        val totalXpToDeduct = logsXp + completionXp + creationXp

        // Delete goal first; foreign-key CASCADE removes its logs too
        goalRepository.deleteGoal(goal.id)

        val profile = userRepository.getUserProfile().first()
        val newXp = maxOf(0, profile.totalXp - totalXpToDeduct)
        userRepository.updateUserProfile(
            profile.copy(
                totalXp = newXp,
                level = UserProfile.levelForXp(newXp),
                totalHabitLogs = maxOf(0, profile.totalHabitLogs - logs.size),
                totalGoalsCompleted = if (goal.isCompleted)
                    maxOf(0, profile.totalGoalsCompleted - 1) else profile.totalGoalsCompleted
            )
        )
    }
}
