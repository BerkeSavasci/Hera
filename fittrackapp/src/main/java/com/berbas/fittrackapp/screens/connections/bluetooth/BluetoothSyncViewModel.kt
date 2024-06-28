package com.berbas.fittrackapp.screens.connections.bluetooth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.fittrackapp.annotations.UserId
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothControllerInterface
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothDeviceDomain
import com.berbas.heraconnectcommon.connection.bluetooth.ConnectionResult
import com.berbas.heraconnectcommon.connection.bluetooth.DataMessage
import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import com.berbas.heraconnectcommon.localData.sensor.FitnessDataDao
import com.berbas.heraconnectcommon.protocolEngine.BluetoothProtocolEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BluetoothSyncViewModel @Inject constructor(
    private val personDao: PersonDao,
    @UserId private val id: Int,
    private val bluetoothController: BluetoothControllerInterface,
    private val fitnessDao: FitnessDataDao,
) : ViewModel() {

    /**
     * A stateflow of all the scanned devices as a list
     */
    val devices: StateFlow<List<BluetoothDeviceDomain>> = bluetoothController.scannedDevices

    private var sentUserData: DataMessage? = null
    private var receivedUserData: DataMessage? = null

    private var serverJob: Job? = null

    private val protocolEngine = BluetoothProtocolEngine()

    /** the data transfer status */
    private val _dataTransferStatus = MutableStateFlow(DataTransferStatus.IDLE)
    val dataTransferStatus: StateFlow<DataTransferStatus> = _dataTransferStatus

    /**
     * Handles the connection to a device, responds to the different results
     */
    fun connectToDevice(device: BluetoothDeviceDomain) {
        _dataTransferStatus.value = DataTransferStatus.IDLE
        Log.d("SyncViewModel", "Status: ${_dataTransferStatus.value}")

        viewModelScope.launch {
            bluetoothController.connectToDevice(device).collect { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        Log.d("BluetoothSyncViewModel", "Connected to device: ${device.name}")

                        val allDataString = fetchAllData()
                        Log.d("BluetoothSyncViewModel", "Sending all data: $allDataString")

                        bluetoothController.trySendMessage(allDataString)
                        _dataTransferStatus.value = DataTransferStatus.SUCCESS
                    }

                    is ConnectionResult.ConnectionFailure -> {
                        _dataTransferStatus.value = DataTransferStatus.FAILURE
                        Log.d("SyncViewModel", "Status: ${_dataTransferStatus.value}")
                        Log.d(
                            "BluetoothSyncViewModel",
                            "Failed to connect to device: ${device.name}"
                        )
                    }

                    is ConnectionResult.TransferSuccess -> {
                        sentUserData = result.message
                        Log.d(
                            "BluetoothSyncViewModel / connect",
                            "Data transfer was successful: $sentUserData"
                        )
                        _dataTransferStatus.value = DataTransferStatus.SUCCESS
                    }
                }
            }
        }
    }

    /**
     * Receives both fitness and person entries form both databases joins them
     * with the separator "*" and returns it
     */
    private suspend fun fetchAllData(): String {
        Log.d("BluetoothSyncViewModel", "Fetching data from database")

        val personData = personDao.getPersonById(id).first()
        Log.d("BluetoothSyncViewModel", "Fetched data from database: $personData")

        val allFitnessData = fitnessDao.getSensorData().first()
        Log.d("BluetoothSyncViewModel", "Fetched data from database: $allFitnessData")


        return "${personData.toString()}*${allFitnessData.toString()}"
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
                        receivedUserData = result.message
                        Log.d(
                            "BluetoothSyncViewModel / server",
                            "Data transfer was successful: $receivedUserData"
                        )
                        val localReceivedUserData = receivedUserData
                        if (localReceivedUserData != null) {
                            Log.d("BluetoothSyncViewModel / server", "Message will get deserialized: $localReceivedUserData")
                            val person =
                                protocolEngine.toPerson(localReceivedUserData.toString())
                            Log.d("BluetoothSyncViewModel / server", "Person Object received: $person")

                            val fitnessData =
                                protocolEngine.toFitnessData(localReceivedUserData.toString())

                            Log.d("BluetoothSyncViewModel / server", "Fitness Object received: $fitnessData")
                            personDao.upsertPerson(person)
                            fitnessDao.insertSensorData(fitnessData)

                            Log.d("BluetoothSyncViewModel / server", "Person Object saved: $person")
                            Log.d("BluetoothSyncViewModel / server", "Fitness Object saved: $fitnessData")
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
    private fun DataMessage.toPerson(): Person {
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
