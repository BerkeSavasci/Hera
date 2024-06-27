package com.berbas.fittrackapp.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
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
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fitnessDataDao: FitnessDataDao,
    private val personDao: PersonDao,
    @UserId private val id: Int
) : ViewModel() {

    val stepCount = MutableStateFlow<Int>(0)
    val stepGoal = MutableStateFlow<Int>(0)

    init {
        viewModelScope.launch {
            fitnessDataDao.getSensorData().collect { fitnessData ->
                val today = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
                val todaySteps = fitnessData?.steps?.find { it.startsWith(today) }
                stepCount.value = todaySteps?.split(": ")?.get(1)?.toInt() ?: 0
            }
        }
        viewModelScope.launch {
            personDao.getPersonById(id).collect { person ->
                stepGoal.value = person.stepGoal
            }
        }
    }
}

