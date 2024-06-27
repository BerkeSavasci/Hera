package com.berbas.heraconnectcommon.wifi

import com.berbas.heraconnectcommon.connection.wifi.UrlWrapper
import com.berbas.heraconnectcommon.connection.wifi.WifiConnection
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.HttpURLConnection
import org.json.JSONObject
import java.io.ByteArrayOutputStream

@RunWith(MockitoJUnitRunner::class)
class WifiConnectionTest {

    private lateinit var wifiConnection: WifiConnection
    private lateinit var mockHttpConnection: HttpURLConnection
    private lateinit var mockUrlWrapper: UrlWrapper
    private lateinit var mockOutputStream: ByteArrayOutputStream

    @Before
    fun setUp() {
        mockHttpConnection = mock(HttpURLConnection::class.java)
        mockUrlWrapper = mock(UrlWrapper::class.java)
        `when`(mockUrlWrapper.openConnection()).thenReturn(mockHttpConnection)

        // Stub getOutputStream() to return a ByteArrayOutputStream
        mockOutputStream = ByteArrayOutputStream()
        `when`(mockHttpConnection.outputStream).thenReturn(mockOutputStream)

        wifiConnection = WifiConnection(mockUrlWrapper)
    }

    @Test
    fun `send should format data correctly`() {
        val testData = "Person(name=John, age=30)"

        // Get the data written to the OutputStream
        val sentJson = wifiConnection.splitData(testData, JSONObject())

        assertEquals("{\"name\":\"John\",\"age\":\"30\"}", sentJson.toString()) // Adjust assertion if needed
    }

    @Test
    fun `send should send correct data`() {
        // Arrange
        val testData = "Person(name=John, age=30)"
        val expectedJson = "{\"name\":\"John\",\"age\":\"30\"}"

        // Act
        runBlocking {
            wifiConnection.send(testData)
        }

        // Assert
        val sentData = mockOutputStream.toString()
        assertEquals(expectedJson, sentData)
    }

    @Test
    fun `receive should fetch data from server and return result`() {
        `when`(mockHttpConnection.responseCode).thenReturn(HttpURLConnection.HTTP_OK)

        // Create a mock InputStream
        val mockInputStream = ByteArrayInputStream("{ \"name\": \"Bob\", \"age\": \"35\" }".toByteArray())

        // Stub getInputStream() to return the mock InputStream
        `when`(mockHttpConnection.inputStream).thenReturn(mockInputStream)

        val result = runBlocking { wifiConnection.receive(123) }

        assertEquals("{ \"name\": \"Bob\", \"age\": \"35\" }", result)
    }

    @Test
    fun `send should handle network errors gracefully`() {
        `when`(mockHttpConnection.outputStream).thenThrow(IOException("Simulated network error"))

        val testData = "Person(name=Alice, age=25)"
        runBlocking { wifiConnection.send(testData) }

        // Verify that an error is logged or handled appropriately
    }

    @Test
    fun `receive should handle non-OK response from server`() {
        `when`(mockHttpConnection.responseCode).thenReturn(HttpURLConnection.HTTP_NOT_FOUND)

        val result = runBlocking { wifiConnection.receive(456) }

        assertEquals("", result) // Or assert an error message if your implementation provides one
    }

    @Test
    fun `receive should handle network errors gracefully`() {
        `when`(mockHttpConnection.inputStream).thenThrow(IOException("Simulated network error"))

        val result = runBlocking { wifiConnection.receive(789) }

        assertEquals("", result) // Or assert an error message if your implementation provides one
    }
}