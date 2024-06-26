package com.berbas.fittrackapp.screens.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fitnessDataDao: FitnessDataDao
) : ViewModel() {
    var stepList = ArrayList<String>()
    var bpmList = ArrayList<String>()
    var sleepList = ArrayList<String>()

    val stepCount = MutableLiveData<Int>()
    init {
        stepList.add(1.toString())
        stepList.add(2.toString())
        stepList.add(3.toString())

        bpmList.add(1.toString())
        bpmList.add(2.toString())
        bpmList.add(3.toString())

        sleepList.add(1.toString())
        sleepList.add(2.toString())
        sleepList.add(3.toString())

        val fitnessData = FitnessData(
            steps = stepList,
            bpm = bpmList,
            sleepTime = sleepList
        )
        viewModelScope.launch {
            fitnessDataDao.insertSensorData(fitnessData)
        }
    }

}
