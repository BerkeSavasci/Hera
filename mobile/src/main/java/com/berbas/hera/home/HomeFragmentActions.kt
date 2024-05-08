package com.berbas.hera.home

/**
 * Interface for HomeFragment actions
 * Acts as a controller for the HomeFragment
 */
interface HomeFragmentActions {

    /**
     * Check sync status
     * check hourly if the data is in sync with the server
     */
    fun checkSyncStatus() {
        // Check sync status
    }

    /**
     * Sync data
     * Sync data with the server
     * if last sync was done by the same device, just upload the data
     * if last sync was done by another device, download the data, merge with local data and upload
     */
    fun syncData(): Boolean {
        // Sync data
        // TODO: check if last sync was done by the same device
        // if yes, upload the data
        // if no, download the data, merge with local data and upload
        // if no data, return false
        // if data, return true

        return false
    }

    /**
     * Download data
     * Download data from the server
     */
    fun downloadData(): FitnessData? {
        // Download data
        return null
    }

    /**
     * Merge data
     * Merge data with the local data
     */
    fun mergeData(newData: FitnessData) {
        // Merge data
    }

    /**
     * Upload data
     * Upload data to the server
     */
    fun uploadData(newData: FitnessData) {
        // Upload data
    }

    /**
     * View data
     * View local data from healthmanager
     */
    fun viewData() {
        // View data
    }

}