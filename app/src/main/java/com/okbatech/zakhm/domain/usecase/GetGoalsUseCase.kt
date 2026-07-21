package com.okbatech.zakhm.domain.usecase

import com.okbatech.zakhm.domain.model.Goal
import com.okbatech.zakhm.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGoalsUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    operator fun invoke(): Flow<List<Goal>> = repository.getGoals()
}
