package com.example.letteblack.screens.onboarding

import androidx.lifecycle.ViewModel
import com.example.letteblack.R
import com.example.letteblack.screens.onboarding.data.Goal
import com.example.letteblack.screens.onboarding.data.ScreenData
import com.example.letteblack.screens.onboarding.data.SubjectChips
import com.example.letteblack.screens.onboarding.data.Subjects
import com.example.letteblack.ui.theme.Amber
import com.example.letteblack.ui.theme.Blue
import com.example.letteblack.ui.theme.Green

class OnBoardingViewModel : ViewModel() {
    val title = "Subject and Domains"
    val subjectsChip: ArrayList<Subjects> = arrayListOf<Subjects>(
        Subjects(
            title = "Science",
            subjects = arrayListOf(
                SubjectChips("Biology", icon = R.drawable.biology, false),
                SubjectChips("Chemistry", icon = R.drawable.chemistry, false),
                SubjectChips("Math", icon = R.drawable.math, false),
                SubjectChips("Physics", icon = R.drawable.physics, false),
            )
        ),
        Subjects(
            title = "Technology",
            subjects = arrayListOf(
                SubjectChips("Programming", icon = R.drawable.programming, false),
                SubjectChips("AI", icon = R.drawable.ai, false),
                SubjectChips("Data Science", icon = R.drawable.analyse, false),
                SubjectChips("Design", icon = R.drawable.design, false),
            )
        ),
        Subjects(
            title = "Humanities",
            subjects = arrayListOf(
                SubjectChips("History", icon = R.drawable.history, false),
                SubjectChips("Philosophy", icon = R.drawable.philosophy, false),
                SubjectChips("Literature", icon = R.drawable.literature, false),
                SubjectChips("Languages", icon = R.drawable.language, false),
            )
        ), Subjects(
            title = "Business",
            subjects = arrayListOf(
                SubjectChips("Marketing", icon = R.drawable.marketing, false),
                SubjectChips("Finance", icon = R.drawable.finance, false),
                SubjectChips("Management", icon = R.drawable.management, false),
            )
        ), Subjects(
            title = "Creative",
            subjects = arrayListOf(
                SubjectChips("Art", icon = R.drawable.art, false),
                SubjectChips("Music", icon = R.drawable.music, false),
                SubjectChips("Media", icon = R.drawable.media, false),
            )
        ), Subjects(
            title = "Exam prep",
            subjects = arrayListOf(
                SubjectChips("SAT", icon = R.drawable.folder_math, false),
                SubjectChips("IELTS", icon = R.drawable.ielts, false),
                SubjectChips("GMAT", icon = R.drawable.gmat, false),
            )
        )
    )
    val items = arrayListOf<ScreenData>(


        ScreenData(
            "Subject and Domains",
            R.raw.subject,
            motiveText = "What topics are you interested in learning?",
            color = Blue,
            R.raw.subject
        ),
        ScreenData(
            "Learning goals",
            R.raw.goal,
            motiveText = "Why are you here?",
            color = Green,
            R.raw.goal
        ),
        ScreenData(
            "Study Time",
            R.raw.goal,
            motiveText = "How and when do you want to learn?",
            color = Amber,
            R.raw.time
        )
    )

    val goals = arrayListOf<Goal>(
        Goal(
            "Pass an exam",
            R.drawable.exam,
            false
        ),
        Goal(
            "Certification",
            R.drawable.certificate,
            false
        ),
        Goal(
            "career skill",
            R.drawable.career,
            false
        ),
        Goal(
            "Improve grade",
            R.drawable.grade,
            false
        ),
        Goal(
            "Personal curiosity",
            R.drawable.hobby,
            false
        ),

        )
}