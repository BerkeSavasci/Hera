package com.berbas.heraconnectcommon.connection.bluetooth

// erlaubt es, dass BluetoothDeviceDomain als BluetoothDevice verwendet wird (BluetoothDevice gibts auch im Andorid SDK)
typealias BluetoothDeviceDomain = BluetoothDevice

data class BluetoothDevice(
    /**
     * The name of the device
     */
    val name: String?,

    /**
     * The MAC address of the device
     */
    val address: String?
)
