package com.okbatech.zakhm.data.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.okbatech.zakhm.domain.model.FrequencyType
import com.okbatech.zakhm.domain.notification.NotificationService
import com.okbatech.zakhm.domain.repository.GoalRepository
import com.okbatech.zakhm.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.Calendar

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val notificationService: NotificationService
) : CoroutineWorker(context, params) {

    companion object {
        const val KEY_TYPE = "type"
        const val TYPE_MORNING = "morning"
        const val TYPE_EVENING = "evening"
    }

    override suspend fun doWork(): Result {
        return when (inputData.getString(KEY_TYPE)) {
            TYPE_MORNING -> checkDailyGoals()
            TYPE_EVENING -> checkStreakAtRisk()
            else -> Result.success()
        }
    }

    private suspend fun checkDailyGoals(): Result {
        val todayStart = startOfDay(System.currentTimeMillis())
        val todayEnd = todayStart + 86_400_000L - 1

        val allGoals = goalRepository.getGoals().first()
        val todayLogs = goalRepository.getHabitLogsForDate(todayStart, todayEnd).first()
        val loggedGoalIds = todayLogs.map { it.goalId }.toSet()

        val pendingCount = allGoals.count { goal ->
            !goal.isCompleted &&
                goal.frequencyType == FrequencyType.DAILY &&
                goal.id !in loggedGoalIds
        }

        if (pendingCount > 0) {
            notificationService.showDailyReminder(pendingCount)
        }
        return Result.success()
    }

    private suspend fun checkStreakAtRisk(): Result {
        val profile = userRepository.getUserProfile().first()
        if (profile.currentStreak <= 0) return Result.success()

        val lastActive = profile.lastActiveDate ?: return Result.success()
        val todayStart = startOfDay(System.currentTimeMillis())
        val lastStart = startOfDay(lastActive)

        if (todayStart > lastStart) {
            notificationService.showStreakAtRisk(profile.currentStreak)
        }
        return Result.success()
    }

    private fun startOfDay(ms: Long): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = ms }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
