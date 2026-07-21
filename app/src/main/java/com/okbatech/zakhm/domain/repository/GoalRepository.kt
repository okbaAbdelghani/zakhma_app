package com.okbatech.zakhm.domain.repository

import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.model.HabitLog
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getGoals(): Flow<List<Goal>>
    fun getGoalById(id: Long): Flow<Goal?>
    suspend fun insertGoal(goal: Goal): Long
    suspend fun updateGoal(goal: Goal)
    suspend fun deleteGoal(goalId: Long)
    fun getHabitLogs(goalId: Long): Flow<List<HabitLog>>
    fun getAllHabitLogs(): Flow<List<HabitLog>>
    suspend fun insertHabitLog(log: HabitLog)
    suspend fun deleteHabitLog(logId: Long)
    fun getHabitLogsForDate(startOfDay: Long, endOfDay: Long): Flow<List<HabitLog>>
}
