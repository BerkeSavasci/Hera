package com.berbas.heraconnectcommon.connection.wifi

/** The state of data transfer via wifi connection */
enum class WifiState {
    // the names are self explanatory
    IDLE,
    IN_PROGRESS,
    SUCCESS,
    FAILURE,
    SERVER_ERROR
}