package com.berbas.hera.presentation.sync.wifi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.hera.annotations.UserId
import com.berbas.heraconnectcommon.connection.wifi.WifiConnectionInterface
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import com.berbas.heraconnectcommon.protocolEngine.WifiProtocolEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WifiSyncViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int,
    private val wifiController: WifiConnectionInterface,
    private val fitnessDao: FitnessDataDao
) : ViewModel(){
    fun send() {
        viewModelScope.launch {
            val personData = personDao.getPersonById(id).first()
            val fitnessData = fitnessDao.getSensorData().first()

            Log.d("WifiSyncViewModel", "Sending person data: $personData")
            val data = WifiProtocolEngine().toDataMessage(personData, fitnessData)
            wifiController.send(data)
        }
    }

    fun receive() {
        viewModelScope.launch {
            val receivedData = wifiController.receive(id)
            if (receivedData.isEmpty()) {
                Log.e("WifiSyncViewModel", "Received data is empty")
            } else {
                Log.d("WifiSyncViewModel", "Received data: $receivedData")
                val dataPair = WifiProtocolEngine().splitReceivedData(receivedData)
                personDao.upsertPerson(dataPair.first)
                fitnessDao.insertSensorData(dataPair.second)
            }
        }
    }
}