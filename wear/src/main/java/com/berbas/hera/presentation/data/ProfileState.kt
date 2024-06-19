package com.berbas.hera.presentation.data

/**
 *  Data class for the state of the profile screen with the default values
 */
data class ProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val birthday: String = "",
    val weight: Double = 0.0,
    val height: Int = 0,
    val stepGoal: Int = 6000,
    val activityGoal: Double = 1.5,
    val isEditingFields: Boolean = false
)
