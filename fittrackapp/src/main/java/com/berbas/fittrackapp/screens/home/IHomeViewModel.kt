package com.berbas.fittrackapp.screens.home

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow

interface IHomeViewModel {
    fun fetchLastSevenDaysSteps()
    abstract fun showInfoDialog(b: Boolean)

    val stepCount: MutableStateFlow<Int>
    val stepGoal: MutableStateFlow<Int>
    val lastSevenDaysSteps: MutableStateFlow<List<Int>>
    val isInfoDialogVisible: MutableState<Boolean>
}