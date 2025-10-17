package com.example.letteblack.presentation.screen.onboarding.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SubjectChips(
    val subjectTitle: String,
    val icon: Int,
    isChecked: Boolean = false
) {
    var isChecked by mutableStateOf(isChecked)
}
