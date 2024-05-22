package com.berbas.hera.connection.connectionFactory

import com.berbas.hera.connection.BluetoothConnection
import com.berbas.hera.connection.Connection

/**
 * Bluetooth connection factory
 */
interface BluetoothConnectionFactory : ConnectionFactory {

    override fun createConnection(): Connection {
        return BluetoothConnection()
    }
}