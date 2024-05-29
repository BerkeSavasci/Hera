package com.berbas.heraconnectcommon.connection

/**
 * defines different connection results
 */
sealed interface ConnectionResult {
    object ConnectionSuccess : ConnectionResult
    data class ConnectionFailure(val message: String) : ConnectionResult
}