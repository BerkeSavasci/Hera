package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.connection.PersonDataMessage
import com.berbas.heraconnectcommon.localData.Person

class WifiProtocolEngine : ProtocolEngine {

    override fun PersonDataMessage.toByteArray(): ByteArray {
        TODO("Serialization not implemented yet.")
    }

    override fun String.toPersonDataMessage(isFromMobile: Boolean): PersonDataMessage {
        TODO("Deserialization not implemented yet.")
    }

    override fun toPersonDataMessage(data: Person): String {
        TODO("Not yet implemented")
    }

    override fun toPerson(data: String): Person {
        TODO("Not yet implemented")
    }
}