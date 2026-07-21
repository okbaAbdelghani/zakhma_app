package com.okbatech.zakhm.domain.model

enum class GoalType(val label: String, val emoji: String) {
    FINANCIAL("Financial", "💰"),
    FITNESS("Fitness", "💪"),
    HABIT("Habit", "🔄"),
    LEARNING("Learning", "📚"),
    MINDFULNESS("Mindfulness", "🧘"),
    CUSTOM("Custom", "⭐")
}

enum class FrequencyType(val label: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    ONE_TIME("One-time")
}
