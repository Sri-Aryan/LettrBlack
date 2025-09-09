package com.example.letteblack.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.letteblack.R

class ReminderWorker(
    private val ctx: Context,
    params: WorkerParameters
) : Worker(ctx, params) {

    override fun doWork(): Result {
        // Basic example: decide what to show
        val prefs = ctx.getSharedPreferences("lettr_prefs", Context.MODE_PRIVATE)
        val streak = prefs.getInt("streak", 0)
        val sessions = prefs.getInt("sessions", 0)

        // Simple logic (customize later)
        val (title, message) = when {
            streak > 0 && streak % 3 == 0 ->
                "🔥 Streak Milestone!" to "You’re on a $streak-day streak. Keep it going!"
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
            .setSmallIcon(R.drawable.ic_notification) // ensure this exists (next step)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
