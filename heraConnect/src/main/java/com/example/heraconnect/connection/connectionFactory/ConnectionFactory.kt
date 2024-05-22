package com.example.heraconnect.connection.connectionFactory

import com.example.heraconnect.connection.Connection

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