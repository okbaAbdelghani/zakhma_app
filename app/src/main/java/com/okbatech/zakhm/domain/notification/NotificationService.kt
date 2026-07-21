package com.okbatech.zakhm.domain.notification

interface NotificationService {
    fun showAchievementUnlocked(title: String, description: String)
    fun showLevelUp(level: Int, levelTitle: String)
    fun showDailyReminder(pendingGoalsCount: Int)
    fun showStreakAtRisk(streakDays: Int)
}
