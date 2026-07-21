package com.okbatech.zakhm.domain.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val xpReward: Int,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)

object Achievements {
    val all = listOf(
        Achievement("first_goal", "First Step", "Create your first goal", "🎯", 50),
        Achievement("streak_7", "On Fire", "Maintain a 7-day streak", "🔥", 100),
        Achievement("streak_30", "Unstoppable", "Maintain a 30-day streak", "⚡", 300),
        Achievement("streak_100", "Legend Streak", "Maintain a 100-day streak", "👑", 1000),
        Achievement("logs_10", "Getting Started", "Log 10 habit completions", "📝", 50),
        Achievement("logs_50", "Consistent", "Log 50 habit completions", "💪", 150),
        Achievement("logs_100", "Century", "Log 100 habit completions", "🏆", 300),
        Achievement("goals_1", "First Victory", "Complete your first goal", "🥇", 200),
        Achievement("goals_5", "Goal Crusher", "Complete 5 goals", "💥", 500),
        Achievement("financial_goal", "Moneybags", "Complete a financial goal", "💰", 200),
        Achievement("fitness_goal", "Athlete", "Complete a fitness goal", "🏃", 200),
        Achievement("habit_goal", "Disciplined", "Complete a habit goal", "🔄", 200),
        Achievement("level_5", "Rising Star", "Reach level 5", "⭐", 250),
        Achievement("level_10", "Master", "Reach level 10", "🌟", 500),
        Achievement("early_bird", "Early Bird", "Log a habit before 7 AM", "🌅", 75),
        Achievement("night_owl", "Night Owl", "Log a habit after 10 PM", "🦉", 75),
    )

    fun getById(id: String) = all.find { it.id == id }
}
