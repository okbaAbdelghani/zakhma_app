package com.okbatech.zakhm.domain.model

data class UserProfile(
    val id: Int = 1,
    val totalXp: Int = 0,
    val level: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActiveDate: Long? = null,
    val totalGoalsCompleted: Int = 0,
    val totalHabitLogs: Int = 0
) {
    val xpForCurrentLevel: Int get() = xpThresholdForLevel(level)
    val xpForNextLevel: Int get() = xpThresholdForLevel(level + 1)
    val xpProgressInLevel: Int get() = totalXp - xpForCurrentLevel
    val xpNeededForNextLevel: Int get() = xpForNextLevel - xpForCurrentLevel
    val levelProgressPercent: Float
        get() = if (xpNeededForNextLevel > 0) xpProgressInLevel.toFloat() / xpNeededForNextLevel else 1f

    val levelTitle: String get() = levelTitles.getOrElse(level - 1) { "Legend" }

    companion object {
        private val levelTitles = listOf(
            "Spark", "Ignite", "Blaze", "Flame", "Inferno",
            "Storm", "Thunder", "Lightning", "Titan", "Legend"
        )

        fun xpThresholdForLevel(level: Int): Int = when (level) {
            1 -> 0
            2 -> 150
            3 -> 400
            4 -> 750
            5 -> 1200
            6 -> 1800
            7 -> 2500
            8 -> 3500
            9 -> 5000
            10 -> 7000
            else -> 7000 + (level - 10) * 2000
        }

        fun levelForXp(xp: Int): Int {
            var level = 1
            while (xp >= xpThresholdForLevel(level + 1)) level++
            return level
        }
    }
}
