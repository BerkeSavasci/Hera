package com.berbas.hera

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
    fun syncData() {
        // Sync data
    }

    /**
     * Download data
     * Download data from the server
     */
    fun downloadData() {
        // Download data
    }

    /**
     * Merge data
     * Merge data with the local data
     */
    fun mergeData() {
        // Merge data
    }

    /**
     * Upload data
     * Upload data to the server
     */
    fun uploadData() {
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