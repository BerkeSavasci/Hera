package com.berbas.heraconnectcommon.connection.bluetooth

/**
 * defines different connection results
 */
sealed interface ConnectionResult {
    object ConnectionSuccess : ConnectionResult
    data class TransferSuccess(val message: PersonDataMessage) : ConnectionResult
    data class ConnectionFailure(val message: String) : ConnectionResult
}

/**
 * defines different server start results
 * TODO: Implement later
 */
sealed interface ServerStartResult {
    object ServerStartSuccess : ServerStartResult
    data class ServerStartFailure(val message: String) : ServerStartResult
}