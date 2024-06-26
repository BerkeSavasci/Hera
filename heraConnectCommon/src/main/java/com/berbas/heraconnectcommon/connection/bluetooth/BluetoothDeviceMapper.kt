package com.berbas.heraconnectcommon.connection.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

/**
 * This function maps a BluetoothDevice object to a BluetoothDeviceDomain object.
 *
 * The connection.BluetoothDevice class is a data class that represents a Bluetooth
 * device with two properties: name and address.
 * The toBluetoothDeviceDomain() function simply creates a new BluetoothDeviceDomain object
 * and initializes its properties with the corresponding values from the BluetoothDevice object.
 *
 * Here is an example of how you would use the toBluetoothDeviceDomain() function:
 * val bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice("00:11:22:33:44:55")
 * val bluetoothDeviceDomain = bluetoothDevice.toBluetoothDeviceDomain()
 *
 * This code would create a BluetoothDeviceDomain object with the name and address of the
 * Bluetooth device with the MAC address "00:11:22:33:44:55".
 */
@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}