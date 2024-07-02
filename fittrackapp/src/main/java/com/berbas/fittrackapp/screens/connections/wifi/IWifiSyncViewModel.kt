package com.berbas.fittrackapp.screens.connections.wifi

import androidx.lifecycle.MutableLiveData
import com.berbas.heraconnectcommon.connection.wifi.WifiState
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for the WifiSyncViewModel.
 *
 * This interface defines the necessary properties and functions for managing the WiFi connection and data transfer.
 *
 * @property wifiState A StateFlow that represents the current state of the WiFi connection.
 * @property errorMessage A MutableLiveData that holds any error message that might occur during the WiFi operations.
 */
interface IWifiSyncViewModel {
    /**
     * A StateFlow that represents the current state of the WiFi connection.
     * The state can be any of the states defined in the WifiState enum.
     */
    val wifiState: StateFlow<WifiState>

    /**
     * A MutableLiveData that holds any error message that might occur during the WiFi operations.
     * This can be used to display error messages to the user.
     */
    val errorMessage: MutableLiveData<String>

    /**
     * Function to send data over the WiFi connection.
     * The implementation should handle all the necessary steps for sending data.
     */
    fun sendData()

    /**
     * Function to receive data over the WiFi connection.
     * The implementation should handle all the necessary steps for receiving data.
     */
    fun receiveData()
}