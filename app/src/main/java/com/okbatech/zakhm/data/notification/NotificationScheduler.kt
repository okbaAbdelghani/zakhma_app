package com.okbatech.zakhm.data.notification

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    private val workManager: WorkManager
) {
    fun scheduleAll() {
        scheduleMorningReminder()
        scheduleEveningReminder()
    }

    private fun scheduleMorningReminder() {
        val request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(minutesUntil(hour = 8, minute = 0), TimeUnit.MINUTES)
            .setInputData(workDataOf(ReminderWorker.KEY_TYPE to ReminderWorker.TYPE_MORNING))
            .build()
        workManager.enqueueUniquePeriodicWork(
            "zakhm_morning_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun scheduleEveningReminder() {
        val request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(minutesUntil(hour = 21, minute = 0), TimeUnit.MINUTES)
            .setInputData(workDataOf(ReminderWorker.KEY_TYPE to ReminderWorker.TYPE_EVENING))
            .build()
        workManager.enqueueUniquePeriodicWork(
            "zakhm_evening_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun minutesUntil(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (!target.after(now)) target.add(Calendar.DAY_OF_MONTH, 1)
        return (target.timeInMillis - now.timeInMillis) / 60_000L
    }
}
