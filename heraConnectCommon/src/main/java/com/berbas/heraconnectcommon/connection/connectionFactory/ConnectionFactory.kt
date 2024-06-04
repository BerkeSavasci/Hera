package com.berbas.heraconnectcommon.connection.connectionFactory

/**
 * Connection factory interface.
 * This interface offers template methods for connection classes.
 *
 */
interface ConnectionFactory {

    /**
     * Create connection
     */
    fun createConnection()
}