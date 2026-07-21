package com.okbatech.zakhm.domain.repository

import com.okbatech.zakhm.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(): Flow<UserProfile>
    suspend fun updateUserProfile(profile: UserProfile)
    fun getUnlockedAchievementIds(): Flow<Set<String>>
    suspend fun unlockAchievement(id: String, unlockedAt: Long)
}
