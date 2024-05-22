package com.berbas.heraconnectcommon.connection.connectionFactory

import com.berbas.heraconnectcommon.connection.BluetoothConnection
import com.berbas.heraconnectcommon.connection.Connection

/**
 * Bluetooth connection factory
 */
interface BluetoothConnectionFactory : ConnectionFactory {

    override fun createConnection(): Connection {
        return BluetoothConnection()
    }
}