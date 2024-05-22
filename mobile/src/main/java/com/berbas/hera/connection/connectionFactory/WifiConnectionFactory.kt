package com.berbas.hera.connection.connectionFactory

import com.berbas.hera.connection.Connection
import com.berbas.hera.connection.WifiConnection

/**
 * WifiConnectionFactory class
 */
interface WifiConnectionFactory : ConnectionFactory {
    override fun createConnection(): Connection {
        return WifiConnection()
    }
}