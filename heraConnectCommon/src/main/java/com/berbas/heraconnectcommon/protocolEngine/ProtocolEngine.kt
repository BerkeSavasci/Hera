package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.connection.PersonDataMessage

interface ProtocolEngine {

    /**
     * Serializes the data to a ByteArray
     */
    fun PersonDataMessage.toByteArray(): ByteArray

    /**
     * Deserializes the data to a PersonDataMessage
     */
    fun String.toPersonDataMessage(isFromMobile: Boolean): PersonDataMessage}