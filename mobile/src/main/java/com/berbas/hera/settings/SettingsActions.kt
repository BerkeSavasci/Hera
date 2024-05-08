package com.berbas.hera.settings

/**
 * Interface for Setting actions.
 * This interface acts as a controller for the SettingsFragment.
 */
interface SettingsActions {

    /**
     * User settings
     * Sets the unit for height and weight.
     */
    fun setHeightUnit(unit: String){

    }

    /**
     * User settings
     * Sets the unit for weight.
     */
    fun setWeightUnit(unit: String){

    }


    /**
     * User settings
     * Sets the unit for distance.
     */
    fun setDistanceUnit(unit: String){

    }

    /**
     * User settings
     * Sets the unit for energy.
     */
    fun setEnergyUnit(unit: String){

    }

    /**
     * User settings
     * Toggle the health connect feature.
     */
    fun toggleHealthConnect(){

    }

    /**
     * User settings
     * Sets the sync interval.
     */
    fun syncInterval(interval: Int){

    }

    /**
     * User settings
     * Deletes all user data.
     */
    fun deleteUserData(){

    }

    /**
     * User settings
     * Sets the notification.
     */
    fun toggleNotification(notification: Array<String>){

    }
}