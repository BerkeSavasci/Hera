package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import org.json.JSONObject

class WifiProtocolEngine : ProtocolEngine {

    override fun toPersonDataMessage(personData: Person, fitnessData: FitnessData): String {
        return "$personData#$fitnessData"
    }

    fun splitReceivedData(data: String): Pair<Person, FitnessData> {
        val jsonObject = JSONObject(data)

        val personData = jsonObject.getJSONObject("person").toString()
        val fitnessData = jsonObject.getJSONObject("fitness").toString()

        val extractedPersonData = toPerson(personData)
        val extractedFitnessData = toFitnessData(fitnessData)

        return Pair(extractedPersonData, extractedFitnessData)
    }

    override fun toFitnessData(data: String): FitnessData {
        if (data.isEmpty()) {
            throw IllegalArgumentException("Data string is empty")
        }
        val jsonObject = JSONObject(data)

        val stepsString = jsonObject.getString("steps")
        val stepsList = ArrayList<String>(stepsString.substring(1, stepsString.length - 1).split(","))

        val bpmString = jsonObject.getString("bpm")
        val bpmList = ArrayList<String>(bpmString.substring(1, bpmString.length - 1).split(","))

        val sleepTimeString = jsonObject.getString("sleepTime")
        val sleepTimeList = ArrayList<String>(sleepTimeString.substring(1, sleepTimeString.length - 1).split(","))

        return FitnessData(
            steps = stepsList,
            bpm = bpmList,
            sleepTime = sleepTimeList,
            cumulativeSteps = jsonObject.getInt("cumulativeSteps")
        )
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