package com.berbas.heraconnectcommon.connection

interface WifiConnectionInterface {
    /**
     * Connect to the device
     */
    fun connect()

    /**
     * Disconnect from the device
     */
    fun disconnect()

    /**
     * Send data to the device
     */
    fun send(data: String)

    /**
     * Receive data from the device
     */
    fun receive(): String
}