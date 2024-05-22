package com.example.heraconnect.connection.connectionFactory

import com.example.heraconnect.connection.Connection
import com.example.heraconnect.connection.WifiConnection

/**
 * WifiConnectionFactory class
 */
interface WifiConnectionFactory : ConnectionFactory {
    override fun createConnection(): Connection {
        return WifiConnection()
    }
}