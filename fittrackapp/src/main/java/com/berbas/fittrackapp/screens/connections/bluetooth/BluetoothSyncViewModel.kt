package com.berbas.fittrackapp.screens.connections.bluetooth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berbas.fittrackapp.annotations.UserId
import com.berbas.fittrackapp.logger.Logger
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
    private val logger: Logger
) : ViewModel(), IBluetoothSyncViewModel  {


    override val devices: StateFlow<List<BluetoothDeviceDomain>> = bluetoothController.scannedDevices
    override val dataTransferStatus: StateFlow<BluetoothConnection.DataTransferStatus> = bluetoothController.dataTransferStatus

    private var sentUserData: DataMessage? = null
    private var receivedUserData: DataMessage? = null

    private var serverJob: Job? = null

    private val protocolEngine = BluetoothProtocolEngine()


    override fun connectToDevice(device: BluetoothDeviceDomain) {
        logger.d("SyncViewModel", "Status: ${dataTransferStatus.value}")

        viewModelScope.launch {
            bluetoothController.connectToDevice(device).collect { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        logger.d("BluetoothSyncViewModel", "Connected to device: ${device.name}")

                        val allDataString = fetchAllData()
                        logger.d("BluetoothSyncViewModel", "Sending all data: $allDataString")

                        bluetoothController.trySendMessage(allDataString)
                    }

                    is ConnectionResult.ConnectionFailure -> {
                        logger.d("SyncViewModel", "Status: ${dataTransferStatus.value}")
                        logger.d(
                            "BluetoothSyncViewModel",
                            "Failed to connect to device: ${device.name}"
                        )
                    }

                    is ConnectionResult.TransferSuccess -> {
                        sentUserData = result.message
                        logger.d(
                            "BluetoothSyncViewModel / connect",
                            "Data transfer was successful: $sentUserData"
                        )
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
        logger.d("BluetoothSyncViewModel", "Fetching data from database")

        val personData = personDao.getPersonById(id).first()
        logger.d("BluetoothSyncViewModel", "Fetched data from database: $personData")

        val allFitnessData = fitnessDao.getSensorData().first()
        logger.d("BluetoothSyncViewModel", "Fetched data from database: $allFitnessData")


        return "${personData}*${allFitnessData}"
    }

    override fun startBluetoothServer() {
        serverJob = viewModelScope.launch {
            bluetoothController.startBluetoothServer().collect { result ->
                when (result) {
                    is ConnectionResult.ConnectionSuccess -> {
                        logger.d("BluetoothSyncViewModel", "Server started successfully")
                    }

                    is ConnectionResult.ConnectionFailure -> {
                        logger.d("BluetoothSyncViewModel", "Server connection failed")
                    }

                    is ConnectionResult.TransferSuccess -> {
                        receivedUserData = result.message
                        logger.d(
                            "BluetoothSyncViewModel / server",
                            "Data transfer was successful: $receivedUserData"
                        )
                        val localReceivedUserData = receivedUserData
                        if (localReceivedUserData != null) {
                            logger.d("BluetoothSyncViewModel / server", "Message will get deserialized: $localReceivedUserData")
                            val person =
                                protocolEngine.toPerson(localReceivedUserData.toString())
                            logger.d("BluetoothSyncViewModel / server", "Person Object received: $person")

                            val fitnessData =
                                protocolEngine.toFitnessData(localReceivedUserData.toString())

                            logger.d("BluetoothSyncViewModel / server", "Fitness Object received: $fitnessData")
                            personDao.upsertPerson(person)
                            fitnessDao.insertSensorData(fitnessData)

                            logger.d("BluetoothSyncViewModel / server", "Person Object saved: $person")
                            logger.d("BluetoothSyncViewModel / server", "Fitness Object saved: $fitnessData")
                        }
                    }
                }
            }
        }
    }

    override fun startDiscovery() {
        viewModelScope.launch {
            bluetoothController.startDiscovery()
        }
    }

    override fun stopBluetoothServer() {
        serverJob?.cancel()
        bluetoothController.closeConnection()
    }

    override fun stopDiscovery() {
        viewModelScope.launch {
            bluetoothController.stopDiscovery()
        }
    }

    override fun release() {
        viewModelScope.launch {
            bluetoothController.release()
        }
    }
}
