package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.connection.PersonDataMessage

/**
 * Handles the serialization and deserialization of data
 */
class BluetoothProtocolEngine : ProtocolEngine {

    override fun String.toPersonDataMessage(isFromMobile: Boolean): PersonDataMessage {
        val name = substringBeforeLast("#")
        val message = substringAfterLast("#")
        return PersonDataMessage(message = message, senderName = name, isFromMobile = isFromMobile)
    }

    /**
     * !IMPORTANT The "#" is used to split the message meaning it can't be used in the message itself
     */
    override fun PersonDataMessage.toByteArray(): ByteArray {
        return "$senderName#$message".encodeToByteArray()
    }
}