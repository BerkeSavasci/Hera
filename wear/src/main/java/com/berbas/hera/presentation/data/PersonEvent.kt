package com.berbas.hera.presentation.data

sealed interface PersonEvent {

    data class SetFirstName(val firstName: String): PersonEvent
    data class SetLastName(val lastName: String): PersonEvent
    data class SetGender(val gender: String): PersonEvent
    data class SetBirthday(val birthday: String): PersonEvent
    data class SetWeight(val weight: Double): PersonEvent
    data class SetHeight(val height: Int): PersonEvent
    data class SetStepGoal(val stepGoal: Int): PersonEvent
    data class SetActivityGoal(val activityGoal: Double): PersonEvent
}