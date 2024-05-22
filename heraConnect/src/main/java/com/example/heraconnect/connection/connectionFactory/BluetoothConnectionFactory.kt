package com.example.heraconnect.connection.connectionFactory

import com.example.heraconnect.connection.BluetoothConnection
import com.example.heraconnect.connection.Connection

/**
 * Bluetooth connection factory
 */
interface BluetoothConnectionFactory : ConnectionFactory {

    override fun createConnection(): Connection {
        return BluetoothConnection()
    }
}