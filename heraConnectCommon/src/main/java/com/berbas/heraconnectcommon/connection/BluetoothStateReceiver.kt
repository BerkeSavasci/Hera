package com.berbas.heraconnectcommon.connection

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

/**
 * This receiver is designed to listen for specific Bluetooth device connection state changes
 * and notify a callback function with the updated state.
 */
class BluetoothStateReceiver(
    private val onStateChanged: (isConnected: Boolean, BluetoothDevice) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE,
                BluetoothDevice::class.java
            )
        } else {
            // For earlier versions of Android, deprecated is ok
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }
        when (intent?.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                onStateChanged(true, device ?: return)
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                onStateChanged(false, device ?: return)
            }
        }
    }
}
