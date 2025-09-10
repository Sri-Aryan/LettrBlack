package com.example.letteblack.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.letteblack.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReminderWorker(
    private val ctx: Context,
    params: WorkerParameters
) : Worker(ctx, params) {

    override fun doWork(): Result {
        val prefs = ctx.getSharedPreferences("lettr_prefs", Context.MODE_PRIVATE)
        val streak = prefs.getInt("streak", 0)
        val sessions = prefs.getInt("sessions", 0)
        val lastDateStr = prefs.getString("lastDate", null)

        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val lastDate = lastDateStr?.let { LocalDate.parse(it, formatter) }

        var newStreak = streak

        // Reset streak if no activity logged for today
        if (lastDate == null || lastDate.isBefore(today)) {
            if (lastDate == null || lastDate.isBefore(today.minusDays(1))) {
                // Missed a day → reset
                newStreak = 0
                prefs.edit().putInt("streak", newStreak).apply()
            }
        }

        // Decide notification
        val (title, message) = when {
            newStreak > 0 && newStreak in listOf(7, 30, 100) ->
                "🔥 Streak Milestone!" to "You’re on a $newStreak-day streak. Keep it going!"

            sessions > 0 && sessions % 10 == 0 ->
                "🌟 Milestone Reached!" to "You’ve completed $sessions sessions! Awesome work!"

            else ->
                "📚 Study Reminder" to "Stay on track! Log today’s study to keep your streak."
        }

        notify(title, message)
        return Result.success()
    }

    private fun notify(title: String, message: String) {
        val notification = NotificationCompat.Builder(ctx, "lettrblack_channel")
            .setSmallIcon(R.drawable.ic_notification) // add this drawable in res/drawable
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
