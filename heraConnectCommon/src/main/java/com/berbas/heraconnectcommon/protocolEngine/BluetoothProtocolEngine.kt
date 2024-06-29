package com.berbas.heraconnectcommon.protocolEngine

import android.util.Log
import com.berbas.heraconnectcommon.connection.bluetooth.DataMessage
import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.sensor.FitnessData

/**
 * Handles the serialization and deserialization of data
 */
class BluetoothProtocolEngine : ProtocolEngine {

    /**
     * Deserializes the data to a UserDataMessage
     *
     * !IMPORTANT The "#" is used to split the message meaning it can't be used in the message itself
     */
    fun String.toUserDataMessage(): DataMessage {
        val name = substringBeforeLast("#")
        val message = substringAfterLast("#")
        return DataMessage(message = message, senderName = name)
    }

    /**
     * Serializes the user data to a ByteArray
     */
    fun DataMessage.toByteArray(): ByteArray {
        return "$senderName#$message".encodeToByteArray()
    }

    override fun toPerson(data: String): Person {
        val personString =
            data.substringBefore("*").substringAfter("Person(").substringBeforeLast(")")
        val personParts = personString.split(", ")

        return Person(
            firstname = personParts[0].substringAfter("="),
            lastname = personParts[1].substringAfter("="),
            birthday = personParts[2].substringAfter("="),
            gender = personParts[3].substringAfter("="),
            height = personParts[4].substringAfter("=").toInt(),
            weight = personParts[5].substringAfter("=").toDouble(),
            stepGoal = personParts[6].substringAfter("=").toInt(),
            activityGoal = personParts[7].substringAfter("=").toDouble(),
        )
    }

    override fun toFitnessData(data: String): FitnessData {
        val fitnessDataString = data.substringAfter("FitnessData(").substringBeforeLast(")")

        val stepsRegex = Regex("steps=\\[(.*?)]")
        val bpmRegex = Regex("bpm=\\[(.*?)]")
        val sleepTimeRegex = Regex("sleepTime=\\[(.*?)]")
        val initialStepCountRegex = Regex("initialStepCount=(\\d+)")
        val cumulativeStepsRegex = Regex("cumulativeSteps=(\\d+)")

        fun extractList(regex: Regex, input: String): ArrayList<String> {
            val matchResult = regex.find(input)
            return if (matchResult != null) {
                ArrayList(matchResult.groupValues[1].split(", ").map { it.trim() })
            } else {
                ArrayList()
            }
        }

        val steps = extractList(stepsRegex, fitnessDataString)
        val bpm = extractList(bpmRegex, fitnessDataString)
        val sleepTime = extractList(sleepTimeRegex, fitnessDataString)

        val initialStepCount = initialStepCountRegex.find(fitnessDataString)?.groupValues?.get(1)?.toInt() ?: 0
        val cumulativeSteps = cumulativeStepsRegex.find(fitnessDataString)?.groupValues?.get(1)?.toInt() ?: 0

        return FitnessData(
            steps = steps,
            bpm = bpm,
            sleepTime = sleepTime,
            initialStepCount = -1,
            cumulativeSteps = cumulativeSteps
        )
    }



    override fun toPersonDataMessage(data: Person): String {
        TODO("Not yet implemented")
    }
}