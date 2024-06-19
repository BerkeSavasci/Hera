package com.berbas.fittrackapp.screens.connections.wifi

import androidx.lifecycle.ViewModel
import com.berbas.fittrackapp.data.annotations.UserId
import com.berbas.heraconnectcommon.connection.WifiConnectionInterface
import com.berbas.heraconnectcommon.localData.PersonDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WifiSyncViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int,
    private val wifiController: WifiConnectionInterface
): ViewModel(){

    fun syncWithServer(){
        val personData = personDao.getPersonById(id)
        wifiController.send(personData.toString())
    }
}