package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.localData.person.Person

interface ProtocolEngine {

    /** Deserializes the data to a Person */
    fun toPerson(data: String): Person

    /** Serializes the data to a String */
    fun toPersonDataMessage(data: Person): String
}
