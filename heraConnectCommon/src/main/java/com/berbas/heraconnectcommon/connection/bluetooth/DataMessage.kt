package com.berbas.heraconnectcommon.connection.bluetooth

/**
 * A wrapper class that contains the message and the sender of the message
 */
data class DataMessage(
    /**
     * The message that is being sent
     */
    val message: String,
    /**
     * The sender of the message
     */
    val senderName: String,
)