package com.berbas.heraconnectcommon.connection.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

/**
 * As soon as the Android OS finds a device it will send a broadcast message.
 * to retrieve the device BroadcastReceiver is used.
 *
 * The class has a private property called onDeviceFound that is a lambda function that takes a
 * BluetoothDevice object as input. This lambda function is called whenever a new Bluetooth device is found.
 */
class FoundDeviceReceiver(
    private val onDeviceFound: (BluetoothDevice) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_DEVICE,
                        BluetoothDevice::class.java
                    )
                } else {
                    // For earlier versions of Android, deprecated is ok
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                }
                device?.let(onDeviceFound)
            }
        }
    }
}
