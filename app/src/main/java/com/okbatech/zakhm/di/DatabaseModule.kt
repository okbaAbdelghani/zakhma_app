package com.okbatech.zakhm.di

import android.content.Context
import androidx.room.Room
import com.okbatech.zakhm.data.local.ZakhmDatabase
import com.okbatech.zakhm.data.local.dao.AchievementDao
import com.okbatech.zakhm.data.local.dao.GoalDao
import com.okbatech.zakhm.data.local.dao.HabitLogDao
import com.okbatech.zakhm.data.local.dao.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ZakhmDatabase =
        Room.databaseBuilder(context, ZakhmDatabase::class.java, "zakhm.db").build()

    @Provides
    fun provideGoalDao(db: ZakhmDatabase): GoalDao = db.goalDao()

    @Provides
    fun provideHabitLogDao(db: ZakhmDatabase): HabitLogDao = db.habitLogDao()

    @Provides
    fun provideUserProfileDao(db: ZakhmDatabase): UserProfileDao = db.userProfileDao()

    @Provides
    fun provideAchievementDao(db: ZakhmDatabase): AchievementDao = db.achievementDao()
}
