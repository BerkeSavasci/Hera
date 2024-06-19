package com.berbas.heraconnectcommon.connection

interface WifiConnectionInterface {
    /**
     * Send data to the device
     */
    fun send(data: String)

    /**
     * Receive data from the device
     */
    fun receive(): String
}