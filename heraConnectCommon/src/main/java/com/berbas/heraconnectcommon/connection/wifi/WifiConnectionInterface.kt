package com.berbas.heraconnectcommon.connection.wifi

import kotlinx.coroutines.flow.StateFlow

interface WifiConnectionInterface {

    /** Send data to the device */
    suspend fun send(data: String)

    /** Receive data from the device */
    suspend fun receive(id: Int): String

    /** returns the state of the wifi data transfer */
    fun getWifiState(): StateFlow<WifiState>
}
