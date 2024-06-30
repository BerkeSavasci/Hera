package com.berbas.fittrackapp.screens.connections.bluetooth

import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothConnection
import com.berbas.heraconnectcommon.connection.bluetooth.BluetoothDeviceDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeBluetoothSyncViewModel : IBluetoothSyncViewModel {
    private val _devices = MutableStateFlow(
        listOf(
            BluetoothDeviceDomain("Device1", "00:11:22:33:44:55"),
            BluetoothDeviceDomain("Device2", "66:77:88:99:AA:BB")
        )
    )
    override val devices: StateFlow<List<BluetoothDeviceDomain>> get() = _devices

    private val _dataTransferStatus = MutableStateFlow(BluetoothConnection.DataTransferStatus.IDLE)
    override val dataTransferStatus: StateFlow<BluetoothConnection.DataTransferStatus> get() = _dataTransferStatus

    override fun connectToDevice(device: BluetoothDeviceDomain) {
        // Fake implementation
    }

    override fun startBluetoothServer() {
        // Fake implementation
    }

    override fun startDiscovery() {
        // Fake implementation
    }

    override fun stopBluetoothServer() {
        // Fake implementation
    }

    override fun stopDiscovery() {
        // Fake implementation
    }

    override fun release() {
        // Fake implementation
    }
}
