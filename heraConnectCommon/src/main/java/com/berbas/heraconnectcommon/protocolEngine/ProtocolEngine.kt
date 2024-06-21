package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.connection.PersonDataMessage
import com.berbas.heraconnectcommon.localData.Person

interface ProtocolEngine {

    /**
     * Serializes the data to a ByteArray
     */
    fun PersonDataMessage.toByteArray(): ByteArray

    /**
     * Deserializes the data to a PersonDataMessage
     */
    fun String.toPersonDataMessage(isFromMobile: Boolean): PersonDataMessage

    /**
     * Deserializes the data to a Person
     */
    fun toPerson(data: String): Person

    /**
     * Serializes the data to a String
     */
    fun toPersonDataMessage(data: Person): String
}
