package com.berbas.fittrackapp.screens.connections.wifi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.fittrackapp.annotations.UserId
import com.berbas.heraconnectcommon.connection.wifi.WifiConnectionInterface
import com.berbas.heraconnectcommon.connection.wifi.WifiState
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import com.berbas.heraconnectcommon.protocolEngine.WifiProtocolEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class WifiSyncViewModel @Inject constructor(
    private val personDao: PersonDao,
    private val fitnessDao: FitnessDataDao,
    @UserId private val id: Int,
    private val wifiController: WifiConnectionInterface
) : ViewModel(), IWifiSyncViewModel {

    override val errorMessage = MutableLiveData<String>()
    override val wifiState: StateFlow<WifiState> = wifiController.getWifiState()

    override fun sendData() {
        viewModelScope.launch {
            val personData = personDao.getPersonById(id).first()
            val fitnessData = fitnessDao.getSensorData().first()

            Log.d("WifiSyncViewModel", "Sending person data: $personData")
            val data = WifiProtocolEngine().toPersonDataMessage(personData, fitnessData)
            wifiController.send(data)
        }
    }

    override fun receiveData() {
        viewModelScope.launch {
            val receivedData = wifiController.receive(id)
            if (receivedData.isEmpty()) {
                Log.e("WifiSyncViewModel", "Received data is empty")
                errorMessage.postValue("Received data is empty")
            } else {
                Log.d("WifiSyncViewModel", "Received data: $receivedData")
                val dataPair = WifiProtocolEngine().splitReceivedData(receivedData)
                personDao.upsertPerson(dataPair.first)
                fitnessDao.insertSensorData(dataPair.second)
            }
        }
    }
}