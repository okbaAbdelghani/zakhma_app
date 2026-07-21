package com.okbatech.zakhm.domain.model

data class HabitLog(
    val id: Long = 0,
    val goalId: Long,
    val value: Float = 1f,
    val note: String = "",
    val completedAt: Long = System.currentTimeMillis(),
    val xpEarned: Int
)
