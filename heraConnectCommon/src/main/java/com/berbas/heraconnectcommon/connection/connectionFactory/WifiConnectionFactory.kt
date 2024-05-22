package com.berbas.heraconnectcommon.connection.connectionFactory

import com.berbas.heraconnectcommon.connection.Connection
import com.berbas.heraconnectcommon.connection.WifiConnection

/**
 * WifiConnectionFactory class
 */
interface WifiConnectionFactory : ConnectionFactory {
    override fun createConnection(): Connection {
        return WifiConnection()
    }
}