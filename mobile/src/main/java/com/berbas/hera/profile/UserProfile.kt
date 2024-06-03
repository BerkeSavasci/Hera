package com.berbas.hera.profile

import java.time.LocalTime
import java.util.Date

/**
 * This class represents the user's profile.
 * Acts as a model for the ProfileFragment.
 * @param stepGoal The step goal for the user.
 * @param sleepTime The time the user wants to go to bed and wake up.
 * @param gender The gender of the user.
 * @param birthDate The birth date of the user.
 * @param weight The weight of the user.
 * @param height The height of the user.
 */
data class UserProfile(
    override var stepGoal: Int,
    override var sleepTime: Pair<LocalTime, LocalTime>,
    override var gender: String,
    override var birthDate: Date,
    override var weight: Double,
    override var height: Double
) : IUserProfile {
    override fun serialize(): String {
        TODO("Not yet implemented")
    }

    override fun deserialize(data: String) {
        TODO("Not yet implemented")
    }

}
