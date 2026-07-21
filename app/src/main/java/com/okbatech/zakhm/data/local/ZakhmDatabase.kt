package com.okbatech.zakhm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.okbatech.zakhm.data.local.dao.AchievementDao
import com.okbatech.zakhm.data.local.dao.GoalDao
import com.okbatech.zakhm.data.local.dao.HabitLogDao
import com.okbatech.zakhm.data.local.dao.UserProfileDao
import com.okbatech.zakhm.data.local.entity.AchievementEntity
import com.okbatech.zakhm.data.local.entity.GoalEntity
import com.okbatech.zakhm.data.local.entity.HabitLogEntity
import com.okbatech.zakhm.data.local.entity.UserProfileEntity

@Database(
    entities = [GoalEntity::class, HabitLogEntity::class, UserProfileEntity::class, AchievementEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ZakhmDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun habitLogDao(): HabitLogDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun achievementDao(): AchievementDao
}
