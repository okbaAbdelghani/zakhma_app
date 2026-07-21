package com.okbatech.zakhm.domain.usecase

import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.model.UserProfile
import com.okbatech.zakhm.domain.repository.GoalRepository
import com.okbatech.zakhm.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateGoalUseCase @Inject constructor(
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val checkAchievementsUseCase: CheckAndUnlockAchievementsUseCase
) {
    suspend operator fun invoke(goal: Goal): Long {
        val id = goalRepository.insertGoal(goal)
        val profile = userRepository.getUserProfile().first()
        val updatedProfile = profile.copy(
            totalXp = profile.totalXp + 25,
            level = UserProfile.levelForXp(profile.totalXp + 25)
        )
        userRepository.updateUserProfile(updatedProfile)
        checkAchievementsUseCase(updatedProfile)
        return id
    }
}
