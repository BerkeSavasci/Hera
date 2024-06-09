package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.connection.PersonDataMessage

class WifiProtocolEngine : ProtocolEngine {

    override fun PersonDataMessage.toByteArray(): ByteArray {
        TODO("Not yet implemented")
    }

    override fun String.toPersonDataMessage(isFromMobile: Boolean): PersonDataMessage {
        TODO("Not yet implemented")
    }
}