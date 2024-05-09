package com.berbas.hera.profile

import java.time.LocalTime
import java.util.Date

interface IUserProfile {
    var stepGoal: Int
    var sleepTime: Pair<LocalTime, LocalTime>
    var gender: String
    var birthDate: Date
    var weight: Double
    var height: Double

    /**
     * Serializes the user profile data to a string for data transfer
     */
    fun serialize(): String

    /**
     * Deserializes the user profile data from a string
     */
    fun deserialize(data: String)
}