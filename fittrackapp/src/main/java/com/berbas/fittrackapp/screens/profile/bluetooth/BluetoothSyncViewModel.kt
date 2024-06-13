package com.berbas.fittrackapp.screens.profile.bluetooth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.heraconnectcommon.connection.BluetoothConnection
import com.berbas.heraconnectcommon.connection.BluetoothDeviceDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BluetoothSyncViewModel(
    private val bluetoothConnection: BluetoothConnection
) : ViewModel() {

    private val _devices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    val devices: StateFlow<List<BluetoothDeviceDomain>> = _devices

    init {
        fetchDevices()
    }

    private fun fetchDevices() {
        viewModelScope.launch {
            _devices.value = bluetoothConnection.scannedDevices.value
        }
    }
}