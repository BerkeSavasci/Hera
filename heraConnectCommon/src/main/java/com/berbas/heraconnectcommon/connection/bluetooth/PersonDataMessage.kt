package com.berbas.heraconnectcommon.connection.bluetooth

/**
 * A wrapper class that contains the message and the sender of the message
 */
data class PersonDataMessage(
    /**
     * The message that is being sent
     */
    val message: String,
    /**
     * The sender of the message
     */
    val senderName: String,

    /**
     * True if the message is from mobile device
     * False if the message is from smart watch
     */
    val isFromMobile: Boolean
)
