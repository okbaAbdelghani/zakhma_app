package com.okbatech.zakhm.data.repository

import com.okbatech.zakhm.data.local.dao.GoalDao
import com.okbatech.zakhm.data.local.dao.HabitLogDao
import com.okbatech.zakhm.data.local.entity.toEntity
import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.model.HabitLog
import com.okbatech.zakhm.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao,
    private val habitLogDao: HabitLogDao
) : GoalRepository {

    override fun getGoals(): Flow<List<Goal>> =
        goalDao.getGoals().map { list -> list.map { it.toDomain() } }

    override fun getGoalById(id: Long): Flow<Goal?> =
        goalDao.getGoalById(id).map { it?.toDomain() }

    override suspend fun insertGoal(goal: Goal): Long =
        goalDao.insert(goal.toEntity())

    override suspend fun updateGoal(goal: Goal) =
        goalDao.update(goal.toEntity())

    override suspend fun deleteGoal(goalId: Long) =
        goalDao.delete(goalId)

    override fun getHabitLogs(goalId: Long): Flow<List<HabitLog>> =
        habitLogDao.getLogsForGoal(goalId).map { list -> list.map { it.toDomain() } }

    override fun getAllHabitLogs(): Flow<List<HabitLog>> =
        habitLogDao.getAllLogs().map { list -> list.map { it.toDomain() } }

    override suspend fun insertHabitLog(log: HabitLog) =
        habitLogDao.insert(log.toEntity())

    override suspend fun deleteHabitLog(logId: Long) =
        habitLogDao.delete(logId)

    override fun getHabitLogsForDate(startOfDay: Long, endOfDay: Long): Flow<List<HabitLog>> =
        habitLogDao.getLogsForDate(startOfDay, endOfDay).map { list -> list.map { it.toDomain() } }
}
