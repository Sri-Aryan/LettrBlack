package com.example.letteblack.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

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

private fun NotificationHelper.Companion.showDailyReminder(applicationContext: Context) {
    TODO("Not yet implemented")
}

class NotificationHelper {
    companion object

}
