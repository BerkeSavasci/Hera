package com.berbas.heraconnectcommon.connection

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
    val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>

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

}