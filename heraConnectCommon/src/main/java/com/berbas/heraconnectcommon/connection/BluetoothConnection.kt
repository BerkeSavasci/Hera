package com.berbas.heraconnectcommon.connection


import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

@SuppressLint("MissingPermission")
class BluetoothConnection(
    private val context: Context
) : BluetoothControllerInterface {

    companion object {
        const val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val _isConnected = MutableStateFlow<Boolean>(false)
    override val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()

    private val _errors = MutableSharedFlow<String>()
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    /**
     * BroadcastReceiver to listen to the found devices
     */
    private val foundDeviceReceiver = FoundDeviceReceiver { device ->
        _scannedDevices.update { devices ->
            val newDevice = device.toBluetoothDeviceDomain()
            if (newDevice in devices) devices else devices + newDevice
        }
    }

    private val bluetoothStateReceiver = BluetoothStateReceiver { isConnected, bluetoothDevice ->
        // update the connected state if only the device is paired
        if (bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == true) {
            _isConnected.update { isConnected }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                _errors.emit("Device is not paired")
            }
        }
    }

    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null

    init {
        updatePairedDevices()
        context.registerReceiver(
            bluetoothStateReceiver,
            IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
            }
        )
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
        context.unregisterReceiver(bluetoothStateReceiver)
        closeConnection()
    }

    override fun startBluetoothServer(): Flow<ConnectionResult> {
        return flow {
            if (!hasPermissions(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No permission to connect to devices: BLUETOOTH_CONNECT")
            }

            Log.d("BluetoothServer", "Starting Server")
            // started the server here
            currentServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                "BluetoothServer",
                UUID.fromString(SERVICE_UUID)
            )
            var shouldLoop = true
            // listen to other connections
            while (shouldLoop) {
                currentClientSocket = try {
                    //accept the connection and save the socket to the currentClientSocket
                    currentServerSocket?.accept()
                } catch (e: IOException) {
                    shouldLoop = false
                    null
                }
                emit(ConnectionResult.ConnectionSuccess)
                // after accepting the connection, close the server socket
                currentClientSocket?.let {
                    currentServerSocket?.close()
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    override fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult> {
        return flow {
            if (!hasPermissions(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No permission to connect to devices: BLUETOOTH_CONNECT")
            }

            val bluetoothDevice = bluetoothAdapter?.getRemoteDevice(device.address)

            // connect to the device
            currentClientSocket = bluetoothDevice
                ?.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_UUID)
                )
            stopDiscovery()

            if(bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == false){


            }

            currentClientSocket?.let { socket ->
                try {
                    socket.connect()
                    emit(ConnectionResult.ConnectionSuccess)

                }catch (e: IOException) {
                    socket.close()
                    currentClientSocket = null
                    emit(ConnectionResult.ConnectionFailure("Failed to connect to the device"))
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    override fun closeConnection() {
        currentClientSocket?.close()
        currentClientSocket = null

        currentServerSocket?.close()
        currentServerSocket = null
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