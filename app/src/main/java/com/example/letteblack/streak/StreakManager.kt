package com.example.letteblack.streak

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

data class StudyResult(
    val streak: Int,
    val sessions: Int,
    val xp: Int,
    val alreadyLoggedToday: Boolean,
    val streakMilestone: Boolean,
    val sessionMilestone: Boolean,
    val xpMilestone: Boolean
)

object StreakManager {
    private const val PREFS_NAME = "study_prefs"
    private const val KEY_LAST_DATE = "last_date"
    private const val KEY_STREAK = "streak"
    private const val KEY_SESSIONS = "sessions"
    private const val KEY_XP = "xp"

    fun recordStudySession(context: Context): StudyResult {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val today = Calendar.getInstance()
        val lastDate = prefs.getLong(KEY_LAST_DATE, -1L)

        val streak = prefs.getInt(KEY_STREAK, 0)
        val sessions = prefs.getInt(KEY_SESSIONS, 0)
        val xp = prefs.getInt(KEY_XP, 0)

        val todayDate = today.get(Calendar.DAY_OF_YEAR)
        val year = today.get(Calendar.YEAR)

        val lastCal = Calendar.getInstance().apply { timeInMillis = lastDate }
        val lastDay = lastCal.get(Calendar.DAY_OF_YEAR)
        val lastYear = lastCal.get(Calendar.YEAR)

        val alreadyLoggedToday = (todayDate == lastDay && year == lastYear)

        var newStreak = streak
        var newSessions = sessions
        var newXp = xp

        var streakMilestone = false
        var sessionMilestone = false
        var xpMilestone = false

        if (!alreadyLoggedToday) {
            // check streak continuity
            newStreak = if (year == lastYear && todayDate - lastDay == 1) {
                streak + 1
            } else {
                1
            }

            newSessions = sessions + 1
            newXp = xp + 10 // assign arbitrary XP per session

            streakMilestone = (newStreak % 7 == 0)
            sessionMilestone = (newSessions % 50 == 0)
            xpMilestone = (newXp % 500 == 0)

            prefs.edit()
                .putLong(KEY_LAST_DATE, today.timeInMillis)
                .putInt(KEY_STREAK, newStreak)
                .putInt(KEY_SESSIONS, newSessions)
                .putInt(KEY_XP, newXp)
                .apply()
        }

        return StudyResult(
            streak = newStreak,
            sessions = newSessions,
            xp = newXp,
            alreadyLoggedToday = alreadyLoggedToday,
            streakMilestone = streakMilestone,
            sessionMilestone = sessionMilestone,
            xpMilestone = xpMilestone
        )
    }
}
