package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import org.json.JSONObject

class WifiProtocolEngine : ProtocolEngine {

    override fun toPersonDataMessage(data: Person): String {
        return data.toString()
    }

    override fun toFitnessData(data: String): FitnessData {
        TODO("Not yet implemented")
    }

    override fun toPerson(data: String): Person {
        if (data.isEmpty()) {
            throw IllegalArgumentException("Data string is empty")
        }
        val jsonObject = JSONObject(data)

        return Person(
            firstname = jsonObject.getString("firstname"),
            lastname = jsonObject.getString("lastname"),
            birthday = jsonObject.getString("birthday"),
            gender = jsonObject.getString("gender"),
            height = jsonObject.getInt("height"),
            weight = jsonObject.getDouble("weight"),
            stepGoal = jsonObject.getInt("stepGoal"),
            activityGoal = jsonObject.getDouble("activityGoal")
        )
    }
}