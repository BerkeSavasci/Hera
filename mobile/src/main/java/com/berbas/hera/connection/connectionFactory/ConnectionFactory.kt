package com.berbas.hera.connection.connectionFactory

import com.berbas.hera.connection.Connection

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