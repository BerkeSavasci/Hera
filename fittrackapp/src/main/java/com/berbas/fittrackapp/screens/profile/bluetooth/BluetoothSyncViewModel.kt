package com.berbas.fittrackapp.screens.profile.bluetooth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.fittrackapp.data.annotations.UserId
import com.berbas.heraconnectcommon.connection.BluetoothControllerInterface
import com.berbas.heraconnectcommon.connection.BluetoothDeviceDomain
import com.berbas.heraconnectcommon.connection.ConnectionResult
import com.berbas.heraconnectcommon.localData.PersonDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothSyncViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int,
    private val bluetoothController: BluetoothControllerInterface
) : ViewModel() {

    private val _devices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    val devices: StateFlow<List<BluetoothDeviceDomain>> = _devices

    fun connectToDevice(device: BluetoothDeviceDomain) {
        viewModelScope.launch {
            bluetoothController.connectToDevice(device).collect { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        // TODO Handle successful connection
                    }

                    is ConnectionResult.ConnectionFailure -> {
                        //TODO  Handle failed connection
                    }

                    is ConnectionResult.TransferSuccess -> {
                        // TODO: Handle successful data transfer
                    }
                }
            }
        }
    }

    fun startBluetoothServer() {
        viewModelScope.launch {
            bluetoothController.startBluetoothServer().collect { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        // TODO: Handle successful server start
                    }

                    is ConnectionResult.ConnectionFailure -> {
                        // TODO: Handle failed server start
                    }

                    is ConnectionResult.TransferSuccess -> {
                        // TODO: Handle successful data transfer
                    }
                }
            }
        }
    }

    fun startDiscovery() {
        viewModelScope.launch {
            bluetoothController.startDiscovery()
        }
    }

    fun stopDiscovery() {
        viewModelScope.launch {
            bluetoothController.stopDiscovery()
        }
    }

    fun release() {
        viewModelScope.launch {
            bluetoothController.release()
        }
    }
}
