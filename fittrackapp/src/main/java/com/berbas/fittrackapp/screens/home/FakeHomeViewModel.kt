package com.berbas.fittrackapp.screens.home

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow

/** A fake [HomeViewModel] that is used to test the [HomeScreen] */
class FakeHomeViewModel : IHomeViewModel {
    override val stepCount = MutableStateFlow(5500)
    override val stepGoal = MutableStateFlow(10000)
    override val lastSevenDaysSteps =
        MutableStateFlow(listOf(5000, 7000, 8000, 6000, 9000, 10000, 11000))
    override val isInfoDialogVisible = mutableStateOf(false)

    override fun fetchLastSevenDaysSteps() {
        lastSevenDaysSteps.value = listOf(5000, 7000, 8000, 6000, 9000, 10000, 11000)
    }

    override fun showInfoDialog(b: Boolean) {
        isInfoDialogVisible.value = true
    }
}