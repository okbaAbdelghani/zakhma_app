package com.okbatech.zakhm.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.okbatech.zakhm.MainActivity
import com.okbatech.zakhm.R
import com.okbatech.zakhm.domain.notification.NotificationService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidNotificationService @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationService {

    companion object {
        const val CHANNEL_REMINDERS = "zakhm_reminders"
        const val CHANNEL_ACHIEVEMENTS = "zakhm_achievements"
        private const val ID_DAILY_REMINDER = 1001
        private const val ID_STREAK_AT_RISK = 1002
        private const val ID_ACHIEVEMENT = 2001
        private const val ID_LEVEL_UP = 2002
    }

    init {
        createChannels()
    }

    private fun createChannels() {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(
            NotificationChannel(CHANNEL_REMINDERS, "Reminders", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Daily habit reminders and streak alerts"
            }
        )
        nm.createNotificationChannel(
            NotificationChannel(CHANNEL_ACHIEVEMENTS, "Achievements", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Level-up and achievement unlocked notifications"
            }
        )
    }

    override fun showDailyReminder(pendingGoalsCount: Int) {
        val text = if (pendingGoalsCount == 1)
            "You have 1 goal to log today. Keep the momentum!"
        else
            "You have $pendingGoalsCount goals to log today. Let's go!"

        show(
            id = ID_DAILY_REMINDER,
            channel = CHANNEL_REMINDERS,
            title = "Good morning! 🌱",
            text = text
        )
    }

    override fun showStreakAtRisk(streakDays: Int) {
        show(
            id = ID_STREAK_AT_RISK,
            channel = CHANNEL_REMINDERS,
            title = "Streak at risk! 🔥",
            text = "Your $streakDays-day streak will reset at midnight. Log something now!"
        )
    }

    override fun showAchievementUnlocked(title: String, description: String) {
        show(
            id = ID_ACHIEVEMENT,
            channel = CHANNEL_ACHIEVEMENTS,
            title = "🏆 Achievement unlocked: $title",
            text = description,
            priority = NotificationCompat.PRIORITY_HIGH
        )
    }

    override fun showLevelUp(level: Int, levelTitle: String) {
        show(
            id = ID_LEVEL_UP,
            channel = CHANNEL_ACHIEVEMENTS,
            title = "⚡ Level $level reached!",
            text = "You are now a $levelTitle. Keep grinding!",
            priority = NotificationCompat.PRIORITY_HIGH
        )
    }

    private fun show(
        id: Int,
        channel: String,
        title: String,
        text: String,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pending = PendingIntent.getActivity(
            context, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(priority)
            .setContentIntent(pending)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(id, notification)
        } catch (_: SecurityException) {
            // POST_NOTIFICATIONS not granted yet — silently skip
        }
    }
}
