package com.berbas.heraconnectcommon

import com.berbas.heraconnectcommon.connection.dbManager.MongoClientConnection
import org.junit.Test

import org.junit.Assert.*
import org.junit.jupiter.api.Assertions.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testMain() {
        // Create a MongoClientConnection object
        val mongoClientConnection = MongoClientConnection()

        // Call the main method and check if an exception is thrown
        assertDoesNotThrow {
            mongoClientConnection.main()
            mongoClientConnection.getMovie()
        }
    }
}