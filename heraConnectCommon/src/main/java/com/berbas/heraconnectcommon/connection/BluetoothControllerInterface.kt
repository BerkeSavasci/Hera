package com.berbas.heraconnectcommon.connection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothControllerInterface {
    val isConnected: StateFlow<Boolean>
    val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
    val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
    val errors: SharedFlow<String>

    /**
     * start the scan for the devices
     */
    fun startDiscovery()

    /**
     * stop the scan for the devices
     */
    fun stopDiscovery()

    /**
     * frees up memory and all the used resources
     */
    fun release()

    /**
     * start the bluetooth server
     * @return Flow<ConnectionResult>
     */
    fun startBluetoothServer(): Flow<ConnectionResult>

    /**
     * connect to the device
     * @param device BluetoothDeviceDomain
     * @return Flow<ConnectionResult>
     */
    fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult>

    /**
     * close the connection
     */
    fun closeConnection()

}