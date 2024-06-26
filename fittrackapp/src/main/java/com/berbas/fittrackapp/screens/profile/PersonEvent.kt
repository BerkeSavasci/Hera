package com.berbas.fittrackapp.screens.profile

/** A sealed class to represent the events that can be triggered in the [ProfileViewModel] */
sealed interface PersonEvent {
    /** The events are self exploratory */
    data class SetFirstName(val firstName: String): PersonEvent
    data class SetLastName(val lastName: String): PersonEvent
    data class SetGender(val gender: String): PersonEvent
    data class SetBirthday(val birthday: String): PersonEvent
    data class SetWeight(val weight: Double): PersonEvent
    data class SetHeight(val height: Int): PersonEvent
    data class SetStepGoal(val stepGoal: Int): PersonEvent
    data class SetActivityGoal(val activityGoal: Double): PersonEvent
}