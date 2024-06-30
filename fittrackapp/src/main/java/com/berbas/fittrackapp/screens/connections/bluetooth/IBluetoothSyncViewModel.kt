package com.berbas.fittrackapp.screens.connections.bluetooth


import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothConnection
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothDeviceDomain
import kotlinx.coroutines.flow.StateFlow

interface IBluetoothSyncViewModel {
    val devices: StateFlow<List<BluetoothDeviceDomain>>
    val dataTransferStatus: StateFlow<BluetoothConnection.DataTransferStatus>

    fun connectToDevice(device: BluetoothDeviceDomain)
    fun startBluetoothServer()
    fun startDiscovery()
    fun stopBluetoothServer()
    fun stopDiscovery()
    fun release()
}