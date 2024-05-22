package com.example.heraconnect.data

/**
 * Interface for DataController

 */
interface DataController {
    /**
     * Fetches data from the protocol engine
     */
    fun fetch(fetchedData: Any): Any

    /**
     * Sends data to the protocol engine
     */
    fun send(sendData: Any, connection: Any)

}