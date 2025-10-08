package com.example.letteblack.screens.onboarding.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Goal(
    val name: String,
    val icon: Int,
    selected: Boolean = false
) {
    var selected by mutableStateOf(selected)

}
