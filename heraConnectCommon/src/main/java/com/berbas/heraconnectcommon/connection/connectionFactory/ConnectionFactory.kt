package com.berbas.heraconnectcommon.connection.connectionFactory

import com.berbas.heraconnectcommon.connection.Connection

/**
 * Connection factory interface.
 * This interface offers template methods for connection classes.
 *
 */
interface ConnectionFactory {

    /**
     * Create connection
     */
    fun createConnection(): Connection
}