package com.berbas.fittrackapp.screens.bluetooth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.fittrackapp.data.annotations.UserId
import com.berbas.heraconnectcommon.connection.BluetoothControllerInterface
import com.berbas.heraconnectcommon.connection.BluetoothDeviceDomain
import com.berbas.heraconnectcommon.connection.ConnectionResult
import com.berbas.heraconnectcommon.connection.PersonDataMessage
import com.berbas.heraconnectcommon.localData.Person
import com.berbas.heraconnectcommon.localData.PersonDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private var receivedPersonData: PersonDataMessage? = null

    private var serverJob: Job? = null

    /** the data transfer status */
    private val _dataTransferStatus = MutableStateFlow(DataTransferStatus.IDLE)
    val dataTransferStatus: StateFlow<DataTransferStatus> = _dataTransferStatus

    /**
     * Handles the connection to a device, responds to the different results
     */
    fun connectToDevice(device: BluetoothDeviceDomain) {
        _dataTransferStatus.value = DataTransferStatus.IDLE

        viewModelScope.launch {
            bluetoothController.connectToDevice(device).collect { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        Log.d("BluetoothSyncViewModel", "Connected to device: ${device.name}")
                        personDao.getPersonById(id).collect { personData ->
                            val personDataString = personData.toString()
                            Log.d(
                                "BluetoothSyncViewModel",
                                "Sending person data: $personDataString"
                            )
                            bluetoothController.trySendMessage(personDataString)
                            _dataTransferStatus.value = DataTransferStatus.SUCCESS
                        }
                    }

                    is ConnectionResult.ConnectionFailure -> {
                        _dataTransferStatus.value = DataTransferStatus.FAILURE
                        Log.d(
                            "BluetoothSyncViewModel",
                            "Failed to connect to device: ${device.name}"
                        )

                    }

                    is ConnectionResult.TransferSuccess -> {
                        receivedPersonData = result.message
                        Log.d(
                            "BluetoothSyncViewModel / connect",
                            "Data transfer was successful: $receivedPersonData"
                        )
                        _dataTransferStatus.value = DataTransferStatus.SUCCESS
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
        _dataTransferStatus.value = DataTransferStatus.IDLE
        serverJob = viewModelScope.launch {
            bluetoothController.startBluetoothServer().collect { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        _dataTransferStatus.value = DataTransferStatus.IDLE
                    }

                    is ConnectionResult.ConnectionFailure -> {
                        _dataTransferStatus.value = DataTransferStatus.FAILURE
                    }

                    is ConnectionResult.TransferSuccess -> {
                        _dataTransferStatus.value = DataTransferStatus.IN_PROGRESS
                        receivedPersonData = result.message
                        Log.d(
                            "BluetoothSyncViewModel / server",
                            "Data transfer was successful: $receivedPersonData"
                        )
                        val localReceivedPersonData = receivedPersonData
                        if (localReceivedPersonData != null) {
                            val person =
                                localReceivedPersonData.toPerson()
                            personDao.upsertPerson(person)
                            _dataTransferStatus.value = DataTransferStatus.SUCCESS
                        }
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

    fun stopBluetoothServer() {
        serverJob?.cancel()
        bluetoothController.closeConnection()
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

    /**
     * Converts the received row data (as String) to a person object and returns it
     */
    private fun PersonDataMessage.toPerson(): Person {
        val personString = message.substringAfter("Person(").substringBeforeLast(")")
        val personParts = personString.split(", ")

        if (personParts.size < 6) {
            _dataTransferStatus.value = DataTransferStatus.FAILURE
            Log.e("BluetoothSyncViewModel / data.toperson", "Not enough data parts: $personParts")
        }

        return Person(
            firstname = personParts[0].substringAfter("="),
            lastname = personParts[1].substringAfter("="),
            birthday = personParts[2].substringAfter("="),
            gender = personParts[3].substringAfter("="),
            height = personParts[4].substringAfter("=").toInt(),
            weight = personParts[5].substringAfter("=").toDouble(),
            stepGoal = personParts[6].substringAfter("=").toInt(),
            activityGoal = personParts[7].substringAfter("=").toDouble(),
        )
    }

    /**
     * A enum class that holds the status of the data transfer
     */
    enum class DataTransferStatus {
        IDLE, STARTED, IN_PROGRESS, SUCCESS, FAILURE
    }

}
