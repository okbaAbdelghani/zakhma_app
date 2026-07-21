package com.okbatech.zakhm.domain.usecase

import com.okbatech.zakhm.domain.model.HabitLog
import com.okbatech.zakhm.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGoalStatsUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    fun getLogsForGoal(goalId: Long): Flow<List<HabitLog>> = repository.getHabitLogs(goalId)
    fun getAllLogs(): Flow<List<HabitLog>> = repository.getAllHabitLogs()
}
