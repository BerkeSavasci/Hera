package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.sensor.FitnessData

interface ProtocolEngine {

    /** Deserializes the data to a [Person] */
    fun toPerson(data: String): Person

    /** Serializes the data to a String */
    fun toPersonDataMessage(data: Person): String

    /** Deserializes the data to a [FitnessData] */
    fun toFitnessData(data: String) : FitnessData
}
