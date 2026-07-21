package com.okbatech.zakhm.domain.usecase

import com.okbatech.zakhm.domain.model.Achievement
import com.okbatech.zakhm.domain.model.Achievements
import com.okbatech.zakhm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAchievementsUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<List<Achievement>> =
        repository.getUnlockedAchievementIds().map { unlockedIds ->
            Achievements.all.map { achievement ->
                achievement.copy(isUnlocked = achievement.id in unlockedIds)
            }
        }
}
