package com.berbas.hera.profile

import com.berbas.heraconnectcommon.localData.Person
import java.time.LocalTime
import java.util.Date

/**
 * Interface for Profile actions.
 * This interface acts as a controller for the ProfileFragment.
 * It defines a set of methods that correspond to different actions a user can perform on their profile.
 */
interface IProfileActionsManagerInterface {

    /**
     * Set the step goal for the user.
     * This could be used to track the user's daily step count goal.
     *
     * @param stepGoal The step goal to set for the user.
     */
    fun setStepGoal(stepGoal: Int)

    /**
     * Set the sleep time for the user.
     * This could be used to track the user's sleep schedule.
     *
     * @param toBed The time the user wants to go to bed.
     * @param wakeUp The time the user wants to wake up.
     */
    fun setSleepTime(toBed: LocalTime, wakeUp: LocalTime)

    /**
     * Set the gender for the user.
     * This could be used for personalizing the user's profile.
     *
     * @param gender The gender of the user.
     */
    fun setGender(gender: String)

    /**
     * Set the birth date for the user.
     * This could be used for calculating the user's age.
     *
     * @param birthDate The birth date of the user.
     */
    fun setBirthDate(birthDate: Date)

    /**
     * Set the weight for the user.
     * This could be used for tracking the user's weight over time.
     *
     * @param weight The weight of the user.
     */
    fun setWeight(weight: Double)

    /**
     * Set the height for the user.
     * This could be used for tracking the user's height over time.
     *
     * @param height The height of the user.
     */
    fun setHeight(height: Double)

    /**
     * View the data of the user.
     * This could be used to display all the user's profile information.
     */
    fun viewData()
}