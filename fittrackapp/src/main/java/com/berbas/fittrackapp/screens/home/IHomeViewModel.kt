package com.berbas.fittrackapp.screens.home

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow

interface IHomeViewModel {
    /** The step count of the user */
    val stepCount: MutableStateFlow<Int>

    /** Users step gaol */
    val stepGoal: MutableStateFlow<Int>

    /** A List of the step counts from the last seven days */
    val lastSevenDaysSteps: MutableStateFlow<List<Int>>

    /** True if [InfoDialog] is visible */
    val isInfoDialogVisible: MutableState<Boolean>

    /** Collects the steps taken from the database */
    fun fetchLastSevenDaysSteps()

    /** Show the info text when the user clicks on the FAB */
    fun showInfoDialog(b: Boolean)
}