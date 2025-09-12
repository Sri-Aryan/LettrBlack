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
                SubjectChips("Programming", icon = R.drawable.biology, false),
                SubjectChips("AI", icon = R.drawable.chemistry, false),
                SubjectChips("Data Science", icon = R.drawable.math, false),
                SubjectChips("Design", icon = R.drawable.physics, false),
            )
        ),
        Subjects(
            title = "Humanities",
            subjects = arrayListOf(
                SubjectChips("History", icon = R.drawable.biology, false),
                SubjectChips("Philosophy", icon = R.drawable.chemistry, false),
                SubjectChips("Literature", icon = R.drawable.math, false),
                SubjectChips("Languages", icon = R.drawable.physics, false),
            )
        ), Subjects(
            title = "Business",
            subjects = arrayListOf(
                SubjectChips("Marketing", icon = R.drawable.biology, false),
                SubjectChips("Finance", icon = R.drawable.chemistry, false),
                SubjectChips("Management", icon = R.drawable.math, false),
            )
        ), Subjects(
            title = "Creative",
            subjects = arrayListOf(
                SubjectChips("Art", icon = R.drawable.biology, false),
                SubjectChips("Music", icon = R.drawable.chemistry, false),
                SubjectChips("Media", icon = R.drawable.math, false),
            )
        ), Subjects(
            title = "Exam prep",
            subjects = arrayListOf(
                SubjectChips("SAT", icon = R.drawable.biology, false),
                SubjectChips("IELTS", icon = R.drawable.chemistry, false),
                SubjectChips("GMAT", icon = R.drawable.math, false),
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
            R.raw.subject
        )
    )

    val goals = arrayListOf<Goal>(
        Goal(
            "Pass an exam",
            R.drawable.math,
            false
        ),
        Goal(
            "Certification",
            R.drawable.math,
            false
        ),
        Goal(
            "career skill",
            R.drawable.math,
            false
        ),
        Goal(
            "Improve grade",
            R.drawable.math,
            false
        ),
        Goal(
            "Personal curiosity",
            R.drawable.math,
            false
        ),

        )
}