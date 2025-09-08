package com.example.letteblack.Worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.letteblack.utils.NotificationHelper

class ReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // 🔹 Show notification when WorkManager runs
        NotificationHelper.showDailyReminder(applicationContext)
        return Result.success()
    }
}
