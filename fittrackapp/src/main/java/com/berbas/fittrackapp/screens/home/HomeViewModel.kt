package com.berbas.fittrackapp.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.fittrackapp.annotations.UserId
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fitnessDataDao: FitnessDataDao,
    private val personDao: PersonDao,
    @UserId private val id: Int
) : ViewModel(), IHomeViewModel {

    /** The step count of the user */
    override val stepCount = MutableStateFlow<Int>(0)

    /** Users step gaol */
    override val stepGoal = MutableStateFlow<Int>(0)

    /** A List of the step counts from the last seven days */
    override val lastSevenDaysSteps = MutableStateFlow<List<Int>>(emptyList())

    /** True if [InfoDialog] is visible */
    override val isInfoDialogVisible = mutableStateOf(false)

    init {
        fetchTodaySteps()
        fetchStepGoal()
        fetchLastSevenDaysSteps()
    }

    /** Collects the steps taken from the database */
    fun fetchTodaySteps() {
        viewModelScope.launch {
            fitnessDataDao.getSensorData().collect { fitnessData ->
                val today = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
                val todaySteps = fitnessData?.steps?.find { it.startsWith(today) }
                stepCount.value = todaySteps?.split(": ")?.get(1)?.toInt() ?: 0
            }
        }
    }

    /** Collects the step goal from the database */
    fun fetchStepGoal() {
        viewModelScope.launch {
            personDao.getPersonById(id).collect { person ->
                stepGoal.value = person.stepGoal
            }
        }
    }

    /** Fetches the step data from the database for the last seven days */
    override fun fetchLastSevenDaysSteps() {
        viewModelScope.launch {
            fitnessDataDao.getSensorData().collect { fitnessData ->
                val lastSevenDays = mutableListOf<Int>()
                for (i in 1..7) {
                    val date = Calendar.getInstance().apply { add(Calendar.DATE, -i) }
                    val formattedDate =
                        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date.time)
                    val steps = fitnessData?.steps?.find { it.startsWith(formattedDate) }
                    lastSevenDays.add(steps?.split(": ")?.get(1)?.toInt() ?: 0)
                }
                lastSevenDaysSteps.value = lastSevenDays
            }
        }
    }

    /** Show the info text when the user clicks on the FAB */
    override fun showInfoDialog(show: Boolean) {
        isInfoDialogVisible.value = show
    }
}
