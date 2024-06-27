package com.berbas.heraconnectcommon.protocolEngine

import com.berbas.heraconnectcommon.localData.person.Person
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertThrows


class WifiProtocolEngineTest {
    private val wifiProtocolEngine = WifiProtocolEngine()

    /**
     * TODO: testing only behaviour of empty data string, as for everything else the build-in JSONObject class is used
     */

    @Test
    fun `toPerson should throw IllegalArgumentException when data string is empty`() {
        val data = ""

        assertThrows(IllegalArgumentException::class.java) {
            wifiProtocolEngine.toPerson(data)
        }
    }
}