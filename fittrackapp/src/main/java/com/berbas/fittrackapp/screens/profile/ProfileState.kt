package com.berbas.fittrackapp.screens.profile

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
    val isEditingFields: Boolean = false
)
