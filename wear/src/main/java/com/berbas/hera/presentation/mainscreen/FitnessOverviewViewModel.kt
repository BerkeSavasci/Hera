package com.berbas.hera.presentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.hera.annotations.UserId
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FitnessOverviewViewModel @Inject constructor(
    private val fitnessDataDao: FitnessDataDao,
    private val personDao: PersonDao,
    @UserId private val id: Int
):  ViewModel() {

    val stepCount = MutableStateFlow(0)
    val stepGoal = MutableStateFlow<Int>(0)


    init {
        fetchTodaySteps()
        observeAndUpdateFitnessData()
    }

    fun observeAndUpdateFitnessData(){
        CoroutineScope(Dispatchers.IO).launch {
            val existingFitnessData = fitnessDataDao.getSensorData().firstOrNull()
            if (existingFitnessData == null) {
                val initialFitnessData = FitnessData(
                    steps = arrayListOf(),
                    bpm = arrayListOf(),
                    sleepTime = arrayListOf(),
                    initialStepCount = 0,
                    cumulativeSteps = 0
                )
                fitnessDataDao.insertSensorData(initialFitnessData)
            }
        }
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
}