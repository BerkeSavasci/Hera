package com.berbas.hera.presentation.sync.wifi

import androidx.lifecycle.ViewModel
import com.berbas.hera.annotations.UserId
import com.berbas.heraconnectcommon.connection.wifi.WifiConnectionInterface
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WifiSyncViewModel @Inject constructor(
    private personDao: PersonDao,
    @UserId private val id: Int,
    private val wifiController: WifiConnectionInterface,
    private val fitnessDao: FitnessDataDao
) : ViewModel(){
    fun send(): () -> Unit {
        return { /* TODO */}
    }

    fun receive(): () -> Unit {
        return { /* TODO */}
    }
}