package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.connection.bluetooth.PersonDataMessage
import com.berbas.heraconnectcommon.localData.person.Person

/**
 * Handles the serialization and deserialization of data
 */
class BluetoothProtocolEngine : ProtocolEngine {

    /**
     * Serializes the data to a ByteArray
     */
    fun String.toPersonDataMessage(isFromMobile: Boolean): PersonDataMessage {
        val name = substringBeforeLast("#")
        val message = substringAfterLast("#")
        return PersonDataMessage(message = message, senderName = name, isFromMobile = isFromMobile)
    }

    override fun toPersonDataMessage(data: Person): String {
        TODO("Not yet implemented")
    }

    override fun toPerson(data: String): Person {
        TODO("Not yet implemented")
    }

    /**
     * Deserializes the data to a PersonDataMessage
     *
     * !IMPORTANT The "#" is used to split the message meaning it can't be used in the message itself
     */
    fun PersonDataMessage.toByteArray(): ByteArray {
        return "$senderName#$message".encodeToByteArray()
    }
}