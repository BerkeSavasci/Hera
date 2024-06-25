package com.berbas.hera.presentation.sync

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.hera.annotations.UserId
import com.berbas.heraconnectcommon.connection.BluetoothControllerInterface
import com.berbas.heraconnectcommon.connection.BluetoothDeviceDomain
import com.berbas.heraconnectcommon.connection.ConnectionResult
import com.berbas.heraconnectcommon.connection.PersonDataMessage
import com.berbas.heraconnectcommon.localData.person.PersonDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int,
    private val bluetoothController: BluetoothControllerInterface
) : ViewModel() {

    /** A live list that contains the scanned devices */
    val devices: StateFlow<List<BluetoothDeviceDomain>> = bluetoothController.scannedDevices

    /** The person data message that is going to be sent */
    private var sendPersonData: PersonDataMessage? = null

    /** The transfer status */
    private val _transferStatus = MutableSharedFlow<String>()
    val transferStatus: SharedFlow<String> = _transferStatus

    /** Starts discovering the nearby bluetooth devices */
    fun startDiscovery() {
        viewModelScope.launch {
            Log.d("SyncViewModel", "Current devices: $devices")
            bluetoothController.startDiscovery()
        }
    }

    /** Stops the discovering of the nearby bluetooth devices */
    fun stopDiscovery() {
        viewModelScope.launch {
            bluetoothController.stopDiscovery()
        }
    }

    /** Connects to the selected device */
    fun connectToDevice(device: BluetoothDeviceDomain) {
        viewModelScope.launch {
            bluetoothController.connectToDevice(device).collect { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        Log.d("BluetoothSyncViewModel", "Connected to device: ${device.name}")
                        personDao.getPersonById(id).collect { personData ->
                            val personDataString = personData.toString()
                            Log.d(
                                "SyncViewModel",
                                "Sending person data: $personDataString"
                            )

                            bluetoothController.trySendMessage(personDataString)
                        }
                    }

                    is ConnectionResult.ConnectionFailure -> {
                        Log.d(
                            "SyncViewModel",
                            "Failed to connect to device: ${device.name}"
                        )
                        _transferStatus.emit("Failed to connect to device: ${device.name}")
                    }

                    is ConnectionResult.TransferSuccess -> {
                        sendPersonData = result.message
                        Log.d(
                            "SyncViewModel",
                            "Data transfer was successful: $sendPersonData"
                        )
                        _transferStatus.emit("Data transfer was successful: $sendPersonData")
                    }
                }
            }
        }
    }
}