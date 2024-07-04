package com.berbas.fittrackapp.screens.connections.bluetooth


import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothConnection
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothDeviceDomain
import kotlinx.coroutines.flow.StateFlow

interface IBluetoothSyncViewModel {
    /** A stateflow of all the scanned devices as a list */
    val devices: StateFlow<List<BluetoothDeviceDomain>>
    val dataTransferStatus: StateFlow<BluetoothConnection.DataTransferStatus>

    /**Handles the connection to a device, responds to the different results */
    fun connectToDevice(device: BluetoothDeviceDomain)

    /**
     * Is called when the Sync screen is opened by the user. Automatically starts a bluetooth server.
     * Handles the results accordingly
     */
    fun startBluetoothServer()

    /** Starts discovering nearby bluetooth devices */
    fun startDiscovery()

    /** Stops the discovering the nearby bluetooth devices */
    fun stopDiscovery()

    /** Stops the bluetooth server */
    fun stopBluetoothServer()

    /** Frees up memory and all the used resources */
    fun release()
}