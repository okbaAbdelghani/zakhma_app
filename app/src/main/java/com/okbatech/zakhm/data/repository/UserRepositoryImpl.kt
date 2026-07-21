package com.okbatech.zakhm.data.repository

import com.okbatech.zakhm.data.local.dao.AchievementDao
import com.okbatech.zakhm.data.local.dao.UserProfileDao
import com.okbatech.zakhm.data.local.entity.AchievementEntity
import com.okbatech.zakhm.data.local.entity.toEntity
import com.okbatech.zakhm.domain.model.UserProfile
import com.okbatech.zakhm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val achievementDao: AchievementDao
) : UserRepository {

    override fun getUserProfile(): Flow<UserProfile> =
        userProfileDao.getProfile().map { it?.toDomain() ?: UserProfile() }

    override suspend fun updateUserProfile(profile: UserProfile) =
        userProfileDao.upsert(profile.toEntity())

    override fun getUnlockedAchievementIds(): Flow<Set<String>> =
        achievementDao.getUnlockedIds().map { it.toSet() }

    override suspend fun unlockAchievement(id: String, unlockedAt: Long) =
        achievementDao.insert(AchievementEntity(id, unlockedAt))
}
