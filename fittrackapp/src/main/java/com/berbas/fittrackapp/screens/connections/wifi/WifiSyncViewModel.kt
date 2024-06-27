package com.berbas.fittrackapp.screens.connections.wifi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.fittrackapp.annotations.UserId
import com.berbas.heraconnectcommon.connection.bluetooth.PersonDataMessage
import com.berbas.heraconnectcommon.connection.wifi.WifiConnectionInterface
import com.berbas.heraconnectcommon.connection.wifi.WifiState
import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.protocolEngine.ProtocolEngine
import com.berbas.heraconnectcommon.protocolEngine.WifiProtocolEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
open class WifiSyncViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int,
    private val wifiController: WifiConnectionInterface
) : ViewModel(){

    val errorMessage = MutableLiveData<String>()
    val wifiState: StateFlow<WifiState> = wifiController.getWifiState()

    fun sendData() {
        viewModelScope.launch {
            val personData = personDao.getPersonById(id).first()
            Log.d("WifiSyncViewModel", "Sending person data: $personData")
            val data = WifiProtocolEngine().toPersonDataMessage(personData)
            wifiController.send(data)
        }
    }

    fun receiveData() {
        viewModelScope.launch {
            val receivedData = wifiController.receive(id)
            if (receivedData.isEmpty()) {
                Log.e("WifiSyncViewModel", "Received data is empty")
                errorMessage.postValue("Received data is empty")
            } else {
                val person = WifiProtocolEngine().toPerson(receivedData)
                personDao.upsertPerson(person)
            }
        }
    }
}