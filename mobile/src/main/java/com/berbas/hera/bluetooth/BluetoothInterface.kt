package com.berbas.hera.bluetooth
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import java.util.UUID

class BluetoothInterface {

//    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//    private var bluetoothSocket: BluetoothSocket? = null

    fun connect(device: BluetoothDevice, context: Context) {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.BLUETOOTH_CONNECT
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
//        bluetoothSocket?.connect()
    }

    fun disconnect() {
//        bluetoothSocket?.close()
    }

    fun read(): Int {
//        return bluetoothSocket?.inputStream?.read() ?: -1
        return 0
    }

    fun write(data: ByteArray) {
//        bluetoothSocket?.outputStream?.write(data)
    }
}