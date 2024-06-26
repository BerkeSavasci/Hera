package com.berbas.heraconnectcommon.connection

interface WifiConnectionInterface {
    abstract val wifiState: Any

    /**
     * Send data to the device
     */
    fun send(data: String)

    /**
     * Receive data from the device
     */
    suspend fun receive(id: Int): String
}