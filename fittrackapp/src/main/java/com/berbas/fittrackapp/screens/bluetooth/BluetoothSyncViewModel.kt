package com.berbas.fittrackapp.screens.bluetooth

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
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

    /**
     * A stateflow of all the scanned devices as a list
     */
    val devices: StateFlow<List<BluetoothDeviceDomain>> = bluetoothController.scannedDevices

    /**
     * Handles the connection to a device, responds to the different results
     */
    fun connectToDevice(device: BluetoothDeviceDomain) {
        viewModelScope.launch {
            bluetoothController.connectToDevice(device).collect { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        Log.d("BluetoothSyncViewModel", "Connected to device: ${device.name}")
                        personDao.getPersonById(id).collect { personData ->
                            val personDataString = personData.toString()
                            Log.d("BluetoothSyncViewModel", "Sending person data: $personDataString")
                            bluetoothController.trySendMessage(personDataString)
                        }
                    }

                    is ConnectionResult.ConnectionFailure -> {
                        Log.d(
                            "BluetoothSyncViewModel",
                            "Failed to connect to device: ${device.name}"
                        )
                    }

                    is ConnectionResult.TransferSuccess -> {
                        Log.d("BluetoothSyncViewModel", "Data transfer was successful")
                    }
                }
            }
        }
    }

    /**
     * Is called when the Sync screen is opened by the user. Automatically starts a bluetooth server.
     * Handles the results accordingly
     */
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

    /**
     * Starts discovering the nearby bluetooth devices
     */
    fun startDiscovery() {
        viewModelScope.launch {
            bluetoothController.startDiscovery()
        }
    }

    /**
     * Stops the discovering of the nearby bluetooth devices
     */
    fun stopDiscovery() {
        viewModelScope.launch {
            bluetoothController.stopDiscovery()
        }
    }

    /**
     * Frees up memory and all the used resources
     */
    fun release() {
        viewModelScope.launch {
            bluetoothController.release()
        }
    }
}
