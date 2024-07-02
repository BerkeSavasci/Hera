package com.berbas.fittrackapp.screens.connections.wifi

import androidx.lifecycle.MutableLiveData
import com.berbas.heraconnectcommon.connection.wifi.WifiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FakeWifiSyncViewModel : IWifiSyncViewModel {
    private val _wifiState = MutableStateFlow(WifiState.IDLE)
    override val wifiState: StateFlow<WifiState> get() = _wifiState

    override val errorMessage = MutableLiveData<String>()

    // Track whether sendData and receiveData were called
    var sendDataCalled = false
    var receiveDataCalled = false

    override fun sendData() {
        sendDataCalled = true
        // Optionally, you can set a specific WifiState here to simulate success/failure
        _wifiState.value = WifiState.SUCCESS // Example
    }

    override fun receiveData() {
        receiveDataCalled = true
    }
}