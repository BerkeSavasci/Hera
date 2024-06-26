package com.berbas.fittrackapp.screens.connections.wifi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.fittrackapp.data.annotations.UserId
import com.berbas.heraconnectcommon.connection.PersonDataMessage
import com.berbas.heraconnectcommon.connection.WifiConnectionInterface
import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.protocolEngine.ProtocolEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class WifiSyncViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int,
    private val wifiController: WifiConnectionInterface
): ViewModel(), ProtocolEngine {
    val errorMessage = MutableLiveData<String>()

    val wifiStatus = wifiController.wifiState
    fun sendData(){
        viewModelScope.launch {
            val personData = personDao.getPersonById(id).first()
            Log.d("WifiSyncViewModel", "Sending person data: $personData")
            val data = toPersonDataMessage(personData)
            wifiController.send(data)
        }
    }

    fun receiveData(){
        viewModelScope.launch {
            val receivedData = wifiController.receive(id)
            if (receivedData.isEmpty()) {
                Log.e("WifiSyncViewModel", "Received data is empty")
                errorMessage.postValue("Received data is empty")
            } else {
                val person = toPerson(receivedData)
                personDao.upsertPerson(person)
            }
        }
    }

    override fun PersonDataMessage.toByteArray(): ByteArray {
        TODO("Not yet implemented")
    }

    override fun String.toPersonDataMessage(isFromMobile: Boolean): PersonDataMessage {
        TODO("Not yet implemented")
    }

    override fun toPersonDataMessage(data: Person): String {
        return data.toString()
    }

    override fun toPerson(data: String): Person {
        if (data.isEmpty()) {
            throw IllegalArgumentException("Data string is empty")
        }
        val jsonObject = JSONObject(data)

        return Person(
            firstname = jsonObject.getString("firstname"),
            lastname = jsonObject.getString("lastname"),
            birthday = jsonObject.getString("birthday"),
            gender = jsonObject.getString("gender"),
            height = jsonObject.getInt("height"),
            weight = jsonObject.getDouble("weight"),
            stepGoal = jsonObject.getInt("stepGoal"),
            activityGoal = jsonObject.getDouble("activityGoal")
        )
    }
}