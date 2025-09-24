package com.example.letteblack.screens.onboarding.data

data class UserPreference(
    val subjectPreference: List<String> = emptyList(),
    val goalPreference: List<String> = emptyList(),
    val studyTimePreference: StudyTimePreference = StudyTimePreference()
)

data class StudyTimePreference(
    var dayPart: String = "",
    var studyHours: String = "",
    var studyStyle: String = ""
)
