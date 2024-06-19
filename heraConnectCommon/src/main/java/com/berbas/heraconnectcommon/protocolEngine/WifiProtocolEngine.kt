package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.connection.PersonDataMessage

class WifiProtocolEngine : ProtocolEngine {

    override fun PersonDataMessage.toByteArray(): ByteArray {
        TODO("Serialization not implemented yet.")
    }

    override fun String.toPersonDataMessage(isFromMobile: Boolean): PersonDataMessage {
        TODO("Deserialization not implemented yet.")
    }
}