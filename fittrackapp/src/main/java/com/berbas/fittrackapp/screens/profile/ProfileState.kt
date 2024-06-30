package com.berbas.fittrackapp.screens.profile

/**
 *  Data class for the state of the profile screen with default values
 */
data class ProfileState(
    val firstName: String = "Placeholder",
    val lastName: String = "Placeholder",
    val gender: String = "Placeholder",
    val birthday: String = "Placeholder",
    val weight: Double = 1.1,
    val height: Int = 1,
    val stepGoal: Int = 6000,
    val activityGoal: Double = 1.5,
)
