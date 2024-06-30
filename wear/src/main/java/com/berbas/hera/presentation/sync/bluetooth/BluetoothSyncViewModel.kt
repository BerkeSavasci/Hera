package com.berbas.hera.presentation.sync.bluetooth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.hera.annotations.UserId
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothConnection
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothControllerInterface
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothDeviceDomain
import com.berbas.heraconnectcommon.connection.bluetooth.ConnectionResult
import com.berbas.heraconnectcommon.connection.bluetooth.DataMessage
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import com.berbas.heraconnectcommon.protocolEngine.BluetoothProtocolEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothSyncViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int,
    private val bluetoothController: BluetoothControllerInterface,
    private val fitnessDao: FitnessDataDao
) : ViewModel() {

    /** A live list that contains the scanned devices */
    val devices: StateFlow<List<BluetoothDeviceDomain>> = bluetoothController.scannedDevices

    /** The data transfer status */
    val dataTransferStatus: StateFlow<BluetoothConnection.DataTransferStatus> = bluetoothController.dataTransferStatus

    /** MutableSharedFlow for transfer status messages */
    private val _transferStatus = MutableSharedFlow<String>()
    val transferStatus: SharedFlow<String> = _transferStatus

    /** DataMessage to be sent */
    private var sentUserData: DataMessage? = null
    private var receivedUserData: DataMessage? = null

    private var serverJob: Job? = null

    private val protocolEngine = BluetoothProtocolEngine()

    /** Starts discovering the nearby Bluetooth devices */
    fun startDiscovery() {
        viewModelScope.launch {
            Log.d("SyncViewModel", "Starting discovery of devices.")
            bluetoothController.startDiscovery()
        }
    }

    /** Stops discovering the nearby Bluetooth devices */
    fun stopDiscovery() {
        viewModelScope.launch {
            bluetoothController.stopDiscovery()
        }
    }

    /** Connects to the selected device */
    fun connectToDevice(device: BluetoothDeviceDomain) {
        Log.d("SyncViewModel", "Status: ${dataTransferStatus.value}")

        viewModelScope.launch {
            bluetoothController.connectToDevice(device).collect { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        Log.d("BluetoothSyncViewModel", "Connected to device: ${device.name}")

                        val allDataString = fetchAllData()
                        Log.d("BluetoothSyncViewModel", "Sending all data: $allDataString")

                        bluetoothController.trySendMessage(allDataString)
                    }
                    is ConnectionResult.ConnectionFailure -> {
                        Log.d("SyncViewModel", "Status: ${dataTransferStatus.value}")

                        Log.d("SyncViewModel", "Failed to connect to device: ${device.name}")
                        _transferStatus.emit("Failed to connect to device: ${device.name}")
                    }
                    is ConnectionResult.TransferSuccess -> {
                        sentUserData = result.message
                        Log.d("SyncViewModel", "Data transfer was successful: $sentUserData")
                        _transferStatus.emit("Data transfer was successful: $sentUserData")
                    }
                }
            }
        }
    }

    /** Fetches all person and fitness data from the database */
    private suspend fun fetchAllData(): String {
        Log.d("BluetoothSyncViewModel", "Fetching data from database")

        val personData = personDao.getPersonById(id).first()
        Log.d("BluetoothSyncViewModel", "Fetched person data: $personData")

        val allFitnessData = fitnessDao.getSensorData().first()
        Log.d("BluetoothSyncViewModel", "Fetched fitness data: $allFitnessData")

        return "${personData}*${allFitnessData}"
    }

    /** Starts a Bluetooth server and handles incoming connections */
    fun startBluetoothServer() {
        serverJob = viewModelScope.launch {
            bluetoothController.startBluetoothServer().collect { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        Log.d("BluetoothSyncViewModel", "Server started successfully")
                    }
                    is ConnectionResult.ConnectionFailure -> {
                        Log.d("BluetoothSyncViewModel", "Server connection failed")
                    }
                    is ConnectionResult.TransferSuccess -> {
                        receivedUserData = result.message
                        Log.d("BluetoothSyncViewModel", "Data transfer was successful: $receivedUserData")
                        processReceivedData(receivedUserData)
                    }
                }
            }
        }
    }

    /** Processes received data and saves it to the database */
    private fun processReceivedData(dataMessage: DataMessage?) {
        viewModelScope.launch {
            val localReceivedUserData = dataMessage ?: return@launch
            Log.d("BluetoothSyncViewModel", "Message received: $localReceivedUserData")

            val person = protocolEngine.toPerson(localReceivedUserData.toString())
            Log.d("BluetoothSyncViewModel", "Deserialized person: $person")

            val fitnessData = protocolEngine.toFitnessData(localReceivedUserData.toString())
            Log.d("BluetoothSyncViewModel", "Deserialized fitness data: $fitnessData")

            personDao.upsertPerson(person)
            fitnessDao.insertSensorData(fitnessData)

            Log.d("BluetoothSyncViewModel", "Person and fitness data saved")
        }
    }

    /** Stops the Bluetooth server */
    fun stopBluetoothServer() {
        serverJob?.cancel()
        bluetoothController.closeConnection()
    }

    /** Releases Bluetooth resources */
    fun release() {
        viewModelScope.launch {
            bluetoothController.release()
        }
    }
}
