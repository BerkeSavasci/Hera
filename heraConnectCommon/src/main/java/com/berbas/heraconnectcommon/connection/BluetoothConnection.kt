package com.berbas.heraconnectcommon.connection

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@SuppressLint("MissingPermission")
class BluetoothConnection(
    private val context: Context
) : Connection, BluetoothController {

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }
    var foundDeviceReceiverRegistered = false

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()

    /**
     * BroadcastReceiver to listen to the found devices
     */
    private val foundDeviceReceiver = FoundDeviceReceiver { device ->
        _scannedDevices.update { devices ->
            val newDevice = device.toBluetoothDeviceDomain()
            if (newDevice in devices) devices else devices + newDevice
        }
        foundDeviceReceiverRegistered = true
    }

    init {
        updatePairedDevices()
    }

    override fun startDiscovery() {
        if (!hasPermissions(Manifest.permission.BLUETOOTH_SCAN)) {
            Log.d("BluetoothConnection", "No permission to scan for devices")
            return
        }
        Log.d("BluetoothConnection", "Starting discovery")
        context.registerReceiver(foundDeviceReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        updatePairedDevices()

        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if (!hasPermissions(Manifest.permission.BLUETOOTH_SCAN)) {
            Log.d("BluetoothConnection", "No permission to stop scanning for devices")
            return
        }
        Log.d("BluetoothConnection", "Stopping discovery")
        bluetoothAdapter?.cancelDiscovery()

    }

    override fun release() {
        context.unregisterReceiver(foundDeviceReceiver)
    }

    override fun connect() {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }

    override fun send(data: String) {
        TODO("Not yet implemented")
    }

    override fun receive(): String {
        TODO("Not yet implemented")
    }


    private fun updatePairedDevices() {
        if (!hasPermissions(Manifest.permission.BLUETOOTH_CONNECT)) {
            return
        }

        bluetoothAdapter?.bondedDevices?.map { it.toBluetoothDeviceDomain() }
            ?.also { devices -> _pairedDevices.update { devices } }
    }

    /**
     * Check if the device has the required permissions
     * returns true if the device has the required permissions, false otherwise
     */
    private fun hasPermissions(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
}