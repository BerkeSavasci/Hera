package com.berbas.heraconnectcommon.connection.dbManager

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class MongoClientConnectionTest {

    @Test
    fun testMain() {
        // Erstellen Sie ein MongoClientConnection-Objekt
        val mongoClientConnection = MongoClientConnection()

        // Rufen Sie die main-Methode auf und überprüfen Sie, ob eine Ausnahme ausgelöst wird
        assertDoesNotThrow {
            mongoClientConnection.main()
        }
    }
}